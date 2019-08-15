package com.kakaopay.banksupport.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Alias("AuthTrd")
public class AuthTrd {
    private String authTrdNo;
    private String userNo;
    private String instDtm;
}
