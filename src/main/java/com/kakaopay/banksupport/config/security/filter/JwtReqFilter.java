package com.kakaopay.banksupport.config.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.banksupport.config.security.handler.AuthFailHandler;
import com.kakaopay.banksupport.config.security.handler.AuthSuccessHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.kakaopay.banksupport.config.SecurityConfig.RESOURCE_URL_PREPIX;

/**
 * Jwt 토큰이 필요한 요청 검사 필터
 */
public class JwtReqFilter extends AbstractAuthenticationProcessingFilter {
    private ObjectMapper objectMapper;

    public JwtReqFilter(AuthSuccessHandler successHandler, AuthFailHandler failHandler, ObjectMapper objectMapper) {
        super(RESOURCE_URL_PREPIX);
        this.objectMapper = objectMapper;
        super.setAuthenticationSuccessHandler(successHandler);
        super.setAuthenticationFailureHandler(failHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        return null;
    }
}
