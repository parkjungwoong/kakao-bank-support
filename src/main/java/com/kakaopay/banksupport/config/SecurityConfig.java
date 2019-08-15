package com.kakaopay.banksupport.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.banksupport.config.security.RestAuthenticationEntryPoint;
import com.kakaopay.banksupport.config.security.Sha256PasswordEncoder;
import com.kakaopay.banksupport.config.security.filter.JwtReqFilter;
import com.kakaopay.banksupport.config.security.filter.LoginReqFilter;
import com.kakaopay.banksupport.config.security.handler.AuthFailHandler;
import com.kakaopay.banksupport.config.security.handler.JwtReqSuccessHandler;
import com.kakaopay.banksupport.config.security.handler.LoginSuccessHandler;
import com.kakaopay.banksupport.config.security.provider.JwtReqProvider;
import com.kakaopay.banksupport.config.security.provider.LoginReqProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //URLs
    public static final String USER_URL_PREPIX = "/user";
    public static final String SIGN_IN_URL = USER_URL_PREPIX+"/signin";
    public static final String SIGN_UP_URL = USER_URL_PREPIX+"/signup";
    public static final String RESOURCE_URL_PREPIX = "/api/**";

    //상수값
    public static final String AUTH_HEADER_KEY = "Authorization";
    public static final String AUTH_HEADER_STR = "Bearer Token";
    public static final String JWT_RULE = "rule";

    @Autowired private ObjectMapper objectMapper;
//
    @Autowired private JwtReqSuccessHandler jwtReqSuccessHandler;
    @Autowired private LoginSuccessHandler loginSuccessHandler;

    @Autowired private AuthFailHandler failHandler;
    @Autowired private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired private LoginReqProvider loginReqProvider;
    @Autowired private JwtReqProvider jwtReqProvider;

    private LoginReqFilter loginReqFilterBuilder(AuthenticationManager authenticationManager) {
        LoginReqFilter loginReqFilter = new LoginReqFilter(loginSuccessHandler, failHandler, objectMapper);
        loginReqFilter.setAuthenticationManager(authenticationManager);
        return loginReqFilter;
    }

    private JwtReqFilter JwtReqFilterBuilder(AuthenticationManager authenticationManager) {
        JwtReqFilter jwtReqFilter = new JwtReqFilter(jwtReqSuccessHandler, failHandler);
        jwtReqFilter.setAuthenticationManager(authenticationManager);
        return jwtReqFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers(new String[]{SIGN_IN_URL, SIGN_UP_URL})
                .permitAll()

                .and()
                .authorizeRequests()
                .antMatchers(RESOURCE_URL_PREPIX)
//                .permitAll()
                .authenticated()

                .and()
                .addFilterBefore(loginReqFilterBuilder(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(JwtReqFilterBuilder(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                ;
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(Arrays.asList(loginReqProvider, jwtReqProvider));
    }
}
