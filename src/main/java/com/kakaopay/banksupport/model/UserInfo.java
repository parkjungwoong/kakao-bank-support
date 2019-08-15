package com.kakaopay.banksupport.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("UserInfo")
public class UserInfo {
    private String userNo;
    private String userId;
    private String userPw;
    private String instId;
    private String instDtm;
    private String updtId;
    private String updtDtm;
    private String useYn;
    private String authTrdNo;
}
