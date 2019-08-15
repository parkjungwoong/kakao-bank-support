package com.kakaopay.banksupport.config.security.provider;

import com.kakaopay.banksupport.config.ComSettings;
import com.kakaopay.banksupport.config.security.dto.UserContext;
import com.kakaopay.banksupport.config.security.jwt.JwtAuthenticationToken;
import com.kakaopay.banksupport.config.security.jwt.JwtParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JwtReqProvider implements AuthenticationProvider {

    @Autowired private ComSettings comSettings;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String rawToken = (String)authentication.getCredentials();

        //토큰 유효성 확인
        Jws<Claims> claimsJws = JwtParser.parseToken(rawToken, comSettings.getJwtSigningKey());

        //권한 파싱 임시
        //List<String> authroties = claimsJws.getBody().get(JWT_RULE, List.class);
        List<String> authroties = new ArrayList<>();
        authroties.add("USER");

        List<GrantedAuthority> authorityList = new ArrayList<>(authroties.size());
        for(String rule : authroties) {
            authorityList.add(new SimpleGrantedAuthority(rule));
        }

        //유저 정보 세팅
        UserContext userContext = UserContext.create(claimsJws.getBody().getSubject(), authorityList);

        return new JwtAuthenticationToken(userContext, userContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return (JwtAuthenticationToken.class.isAssignableFrom(aClass));
    }
}
