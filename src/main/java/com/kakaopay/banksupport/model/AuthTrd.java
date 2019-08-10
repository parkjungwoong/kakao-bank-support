package com.kakaopay.banksupport.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("AuthTrd")
public class AuthTrd {
    private String authTrdNo;
    private String userNo;
    private String instDtm;
}
