package com.kakaopay.banksupport.config.security.filter;

import com.kakaopay.banksupport.common.constant.ResCode;
import com.kakaopay.banksupport.config.SecurityConfig;
import com.kakaopay.banksupport.config.security.exception.ComAuthException;
import com.kakaopay.banksupport.config.security.jwt.JwtAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.kakaopay.banksupport.config.SecurityConfig.RESOURCE_URL_PREPIX;

/**
 * Jwt 토큰이 필요한 요청 검사 필터
 */
@Slf4j
public class JwtReqFilter extends AbstractAuthenticationProcessingFilter {

    public JwtReqFilter(AuthenticationSuccessHandler successHandler, AuthenticationFailureHandler failHandler) {
        super(RESOURCE_URL_PREPIX);
        super.setAuthenticationSuccessHandler(successHandler);
        super.setAuthenticationFailureHandler(failHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException, IOException, ServletException {
        //인증 해더 있는지 검사
        String authHeader = req.getHeader(SecurityConfig.AUTH_HEADER_KEY);

        if(StringUtils.isEmpty(authHeader)) throw new ComAuthException(ResCode.E009);

        String rawToken = authHeader.substring(SecurityConfig.AUTH_HEADER_STR.length());

        return getAuthenticationManager().authenticate(new JwtAuthenticationToken(rawToken));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        log.info("successfulAuthentication");
        chain.doFilter(request, response);
    }
}
