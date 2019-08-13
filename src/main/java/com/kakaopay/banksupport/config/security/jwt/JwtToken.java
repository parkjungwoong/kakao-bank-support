package com.kakaopay.banksupport.config.security.jwt;

import com.kakaopay.banksupport.model.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JwtToken {
    String token;
    UserInfo userInfo;
}
