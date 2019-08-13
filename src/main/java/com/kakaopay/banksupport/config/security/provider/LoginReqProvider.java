package com.kakaopay.banksupport.config.security.provider;

import com.kakaopay.banksupport.common.constant.ErrorCode;
import com.kakaopay.banksupport.config.security.exception.ComAuthException;
import com.kakaopay.banksupport.dto.SignInDTO;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class LoginReqProvider implements AuthenticationProvider {

    private UserService userService;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public LoginReqProvider(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String id = (String) authentication.getPrincipal();
        final String pw = (String) authentication.getCredentials();

        UserInfo userInfo = userService.selectUserInfo(SignInDTO.builder().id(id).pw(pw).build());

        if(userInfo == null) throw new ComAuthException(ErrorCode.E004);
        if(passwordEncoder.matches(pw, userInfo.getUserPw()))  throw new ComAuthException(ErrorCode.E004);

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
