package com.kakaopay.banksupport.config.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.banksupport.common.constant.ResCode;
import com.kakaopay.banksupport.config.security.exception.ComAuthException;
import com.kakaopay.banksupport.dto.req.SignInDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

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

    private AuthenticationFailureHandler failureHandler;

    public LoginReqFilter(AuthenticationSuccessHandler successHandler, AuthenticationFailureHandler failHandler, ObjectMapper objectMapper) {
        super(SIGN_IN_URL);
        this.objectMapper = objectMapper;
        super.setAuthenticationSuccessHandler(successHandler);
        this.failureHandler = failHandler;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException, IOException, ServletException {
        log.info("LoginReqFilter");
        if(!HttpMethod.POST.name().equals(req.getMethod())) {
            throw new ComAuthException(ResCode.E003);
        }

        SignInDTO signInDTO;
        try {
            signInDTO = objectMapper.readValue(req.getReader(), SignInDTO.class);
        } catch (Exception e) {
            throw new ComAuthException(ResCode.E005);
        }


        if(StringUtils.isEmpty(signInDTO.getId()) || StringUtils.isEmpty(signInDTO.getPw())) {
            throw new ComAuthException(ResCode.E005);
        }

        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(signInDTO.getId(),signInDTO.getPw()));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request,response,failed);
    }
}
