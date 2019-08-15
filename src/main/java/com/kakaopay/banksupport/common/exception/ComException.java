package com.kakaopay.banksupport.common.exception;

import com.kakaopay.banksupport.common.constant.ResCode;
import lombok.Getter;

public class ComException extends RuntimeException {

    @Getter
    private ResCode resCode;

    public ComException(ResCode resCode, Throwable e){
        super(resCode.getMessage(), e);
        this.resCode = resCode;
    }

    public ComException(ResCode resCode){
        super(resCode.getMessage());
        this.resCode = resCode;
    }

}
