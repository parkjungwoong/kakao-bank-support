package com.kakaopay.banksupport.dto;

import com.kakaopay.banksupport.common.constant.ErrorCode;
import lombok.Builder;

@Builder
public class ComDTO {
    private String resCode;
    private String resMsg;

    public ComDTO(ErrorCode errorCode) {
        this.resCode = errorCode.name();
        this.resMsg = errorCode.getMessage();
    }
}
