package com.kakaopay.banksupport.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ComSettings {
    @Value("${com.initDataFilePath}")
    private String initDataFilePath;

    @Value("${com.jwt.jwtIssuer}")
    private String jwtIssuer;

    @Value("${com.jwt.jwtAccessExpTime}")
    private long jwtAccessExpTime;

    @Value("${com.jwt.jwtRefreshExpTime}")
    private long jwtRefreshExpTime;

    @Value("${com.jwt.jwtSigningKey}")
    private String jwtSigningKey;


}
