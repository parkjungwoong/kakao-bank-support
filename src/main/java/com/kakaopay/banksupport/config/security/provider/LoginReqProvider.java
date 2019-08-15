package com.kakaopay.banksupport.config.security.provider;

import com.kakaopay.banksupport.common.constant.ResCode;
import com.kakaopay.banksupport.config.security.Sha256PasswordEncoder;
import com.kakaopay.banksupport.config.security.exception.ComAuthException;
import com.kakaopay.banksupport.dto.req.SignInDTO;
import com.kakaopay.banksupport.model.UserInfo;
import com.kakaopay.banksupport.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class LoginReqProvider implements AuthenticationProvider {

    private UserService userService;
    private Sha256PasswordEncoder passwordEncoder;

    @Autowired
    public LoginReqProvider(UserService userService, Sha256PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("LoginReqProvider");
        final String id = (String) authentication.getPrincipal();
        final String pw = (String) authentication.getCredentials();

        UserInfo userInfo = userService.selectUserInfo(SignInDTO.builder().id(id).pw(pw).build());

        if(userInfo == null) throw new ComAuthException(ResCode.E004);
        if(!passwordEncoder.matches(pw, userInfo.getUserPw()))  throw new ComAuthException(ResCode.E004);

        //todo: 권한 설정하기
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("USER"));

        return new UsernamePasswordAuthenticationToken(userInfo, null, authorityList);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass));
    }
}
