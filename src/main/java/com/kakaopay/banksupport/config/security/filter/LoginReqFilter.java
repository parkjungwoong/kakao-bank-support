package com.kakaopay.banksupport.config.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.banksupport.common.constant.ErrorCode;
import com.kakaopay.banksupport.config.security.exception.ComAuthException;
import com.kakaopay.banksupport.config.security.handler.AuthFailHandler;
import com.kakaopay.banksupport.config.security.handler.AuthSuccessHandler;
import com.kakaopay.banksupport.dto.SignInDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.kakaopay.banksupport.config.SecurityConfig.SIGN_IN_URL;

/**
 * Jwt 토큰이 필요 없는 요청 검사 필터
 */
@Slf4j
public class LoginReqFilter extends AbstractAuthenticationProcessingFilter {

    private ObjectMapper objectMapper;

    public LoginReqFilter(AuthSuccessHandler successHandler, AuthFailHandler failHandler, ObjectMapper objectMapper) {
        super(SIGN_IN_URL);
        this.objectMapper = objectMapper;
        super.setAuthenticationSuccessHandler(successHandler);
        super.setAuthenticationFailureHandler(failHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException, IOException, ServletException {

        if(!HttpMethod.POST.name().equals(req.getMethod())) {
            throw new ComAuthException(ErrorCode.E003);
        }

        SignInDTO signInDTO = objectMapper.readValue(req.getReader(), SignInDTO.class);

        if(StringUtils.isEmpty(signInDTO.getId()) || StringUtils.isEmpty(signInDTO.getPw())) {
            throw new ComAuthException(ErrorCode.E005);
        }

        return new UsernamePasswordAuthenticationToken(signInDTO.getId(),signInDTO.getPw());
    }
}
