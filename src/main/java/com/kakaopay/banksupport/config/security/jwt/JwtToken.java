package com.kakaopay.banksupport.config.security.jwt;

import com.kakaopay.banksupport.config.security.dto.UserContext;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JwtToken {
    String token;
    UserContext userContext;
}
