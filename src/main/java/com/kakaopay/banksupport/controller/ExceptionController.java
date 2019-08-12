package com.kakaopay.banksupport.controller;

import com.kakaopay.banksupport.common.constant.ErrorCode;
import com.kakaopay.banksupport.common.exception.ComException;
import com.kakaopay.banksupport.dto.ComDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@ControllerAdvice
@RestController
public class ExceptionController {

    @ExceptionHandler(ComException.class)
    public ComDTO sendComErrorMsg(ComException e) {
        //todo:
        return new ComDTO(e.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    public ComDTO sendErrorMsg(Exception e) {
        //todo:
        return new ComDTO(ErrorCode.E999);
    }


}
