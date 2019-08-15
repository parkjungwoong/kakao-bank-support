package com.kakaopay.banksupport.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.banksupport.common.constant.ResCode;
import com.kakaopay.banksupport.config.security.jwt.JwtFactory;
import com.kakaopay.banksupport.config.security.jwt.JwtToken;
import com.kakaopay.banksupport.dto.res.SignInResDTO;
import com.kakaopay.banksupport.model.AuthTrd;
import com.kakaopay.banksupport.model.UserInfo;
import com.kakaopay.banksupport.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 로그인 성공 핸들러
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired private ObjectMapper objectMapper;
    @Autowired private JwtFactory jwtFactory;
    @Autowired private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication authentication) throws IOException, ServletException {
        //로그인 성공시 로직
        UserInfo userInfo = (UserInfo)authentication.getPrincipal();

        res.setStatus(HttpStatus.OK.value());
        res.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        //인증 내역 저장
        userService.insertAuthTrd(AuthTrd.builder()
                .userNo(userInfo.getUserNo())
                .build());

        //토큰 발급
        JwtToken accessToken = jwtFactory.getAccessToken(userInfo);
        JwtToken refreshToken = jwtFactory.getRefreshToken(userInfo);

        SignInResDTO resDTO = SignInResDTO.builder()
                .accessToken(accessToken.getToken())
                .refreshToken(refreshToken.getToken())
                .build();

        resDTO.setResCode(ResCode.S000);

        objectMapper.writeValue(res.getWriter(),resDTO);
    }
}
