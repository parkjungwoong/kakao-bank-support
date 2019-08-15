package com.kakaopay.banksupport.config.security;

import com.kakaopay.banksupport.config.security.handler.AuthFailHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Autowired
    private AuthFailHandler failHandler;

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        log.error("ExceptionEntryPoint");
        failHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
    }
}
