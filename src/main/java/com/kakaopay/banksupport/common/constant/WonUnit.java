package com.kakaopay.banksupport.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  WonUnit {
    EOG_WON("억원",8),
    BAEG_MAN_WON("백만원",6)
    ;

    private String korUnit;
    private int zeroCnt;

    public String getZeroCnt() {
        StringBuilder sb = new StringBuilder();

        for(int i=0; zeroCnt>i; i++) {
            sb.append("0");
        }

        return sb.toString();
    }
}
