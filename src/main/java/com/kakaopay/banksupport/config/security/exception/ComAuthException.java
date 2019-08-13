package com.kakaopay.banksupport.config.security.exception;

import com.kakaopay.banksupport.common.constant.ErrorCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

public class ComAuthException extends AuthenticationException {

    @Getter
    private ErrorCode errorCode;

    public ComAuthException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ComAuthException(ErrorCode errorCode, Throwable t) {
        super(errorCode.getMessage(), t);
        this.errorCode = errorCode;
    }
}
