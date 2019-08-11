package com.kakaopay.banksupport.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ComSettings {
    @Value("${com.initDataFilePath}")
    private String initDataFilePath;
}
