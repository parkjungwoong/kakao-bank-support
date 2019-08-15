package com.kakaopay.banksupport.config.security.exception;

import com.kakaopay.banksupport.common.constant.ResCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

public class ComAuthException extends AuthenticationException {

    @Getter
    private ResCode resCode;

    public ComAuthException(ResCode resCode) {
        super(resCode.getMessage());
        this.resCode = resCode;
    }

    public ComAuthException(ResCode resCode, Throwable t) {
        super(resCode.getMessage(), t);
        this.resCode = resCode;
    }
}
