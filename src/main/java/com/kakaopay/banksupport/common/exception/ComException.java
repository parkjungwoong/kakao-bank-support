package com.kakaopay.banksupport.common.exception;

import com.kakaopay.banksupport.common.constant.ErrorCode;
import lombok.Getter;

public class ComException extends RuntimeException {

    @Getter
    private ErrorCode errorCode;

    public ComException(ErrorCode errorCode, Throwable e){
        super(errorCode.getMessage(), e);
        this.errorCode = errorCode;
    }

    public ComException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
