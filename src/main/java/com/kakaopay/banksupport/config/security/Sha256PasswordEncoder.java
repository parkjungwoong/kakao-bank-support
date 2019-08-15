package com.kakaopay.banksupport.config.security;

import com.kakaopay.banksupport.common.CommonUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Sha256PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return CommonUtils.sha256(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return CommonUtils.sha256(rawPassword.toString()).equals(encodedPassword);
    }
}
