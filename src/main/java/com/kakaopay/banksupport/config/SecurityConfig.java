package com.kakaopay.banksupport.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.banksupport.config.security.filter.JwtReqFilter;
import com.kakaopay.banksupport.config.security.filter.LoginReqFilter;
import com.kakaopay.banksupport.config.security.handler.AuthFailHandler;
import com.kakaopay.banksupport.config.security.handler.AuthSuccessHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String USER_URL_PREPIX = "/user";
    public static final String SIGN_IN_URL = USER_URL_PREPIX+"/signin";
    public static final String SIGN_UP_URL = USER_URL_PREPIX+"/signup";

    public static final String RESOURCE_URL_PREPIX = "/api/**";

    @Autowired private AuthSuccessHandler successHandler;
    @Autowired private AuthFailHandler failHandler;
    @Autowired private ObjectMapper objectMapper;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .exceptionHandling()

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
                .authenticated()

                .and()
                .addFilterBefore(new LoginReqFilter(successHandler, failHandler, objectMapper), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtReqFilter(successHandler, failHandler, objectMapper), UsernamePasswordAuthenticationFilter.class)
                ;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
