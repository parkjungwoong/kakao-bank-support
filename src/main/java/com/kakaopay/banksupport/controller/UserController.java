package com.kakaopay.banksupport.controller;

import com.kakaopay.banksupport.config.SecurityConfig;
import com.kakaopay.banksupport.dto.req.SignupDTO;
import com.kakaopay.banksupport.dto.req.TokenRefreshDTO;
import com.kakaopay.banksupport.dto.res.ComDTO;
import com.kakaopay.banksupport.dto.res.SignInResDTO;
import com.kakaopay.banksupport.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/signup")
    public ComDTO signup(@RequestBody SignupDTO dto) {
        return userService.signup(dto);
    }

    @PostMapping("/api/refreshToken")
    public SignInResDTO tokenRefresh(@RequestBody TokenRefreshDTO dto) {
        return userService.tokenRefresh(dto);
    }
}
