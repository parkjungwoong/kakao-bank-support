package com.kakaopay.banksupport.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.banksupport.common.constant.ResCode;
import com.kakaopay.banksupport.config.security.exception.ComAuthException;
import com.kakaopay.banksupport.dto.res.ComDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class AuthFailHandler implements AuthenticationFailureHandler {

    @Autowired private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        log.error("인증 실패", e);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        ComDTO comDTO;

        if(e instanceof ComAuthException) {
            comDTO = new ComDTO(((ComAuthException) e).getResCode());
        } else {
            comDTO = new ComDTO(ResCode.E999);
        }

        objectMapper.writeValue(response.getWriter(), comDTO);
    }
}
