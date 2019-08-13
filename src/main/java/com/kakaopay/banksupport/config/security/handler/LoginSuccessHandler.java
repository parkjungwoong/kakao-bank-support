package com.kakaopay.banksupport.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.banksupport.config.security.jwt.JwtFactory;
import com.kakaopay.banksupport.config.security.jwt.JwtToken;
import com.kakaopay.banksupport.dto.SignInDTO;
import com.kakaopay.banksupport.model.UserInfo;
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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication authentication) throws IOException, ServletException {
        //todo: 로그인 성공시 로직
        UserInfo userInfo = (UserInfo)authentication.getPrincipal();

        JwtToken accessToken = jwtFactory.getAccessToken(userInfo);
        JwtToken refreshToken = jwtFactory.getRefreshToken(userInfo);

        res.setStatus(HttpStatus.OK.value());
        res.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        objectMapper.writeValue(res.getWriter(),
                SignInDTO.builder()
                        .accessToken(accessToken.getToken())
                        .refreshToken(refreshToken.getToken())
                        .build()
        );
    }
}
