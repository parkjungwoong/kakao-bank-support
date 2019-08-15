package com.kakaopay.banksupport.controller;

import com.kakaopay.banksupport.common.constant.ResCode;
import com.kakaopay.banksupport.common.exception.ComException;
import com.kakaopay.banksupport.dto.res.ComDTO;
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
        log.error("ExceptionController",e);
        return new ComDTO(e.getResCode());
    }

    @ExceptionHandler(Exception.class)
    public ComDTO sendErrorMsg(Exception e) {
        log.error("ExceptionController",e);
        return new ComDTO(ResCode.E999);
    }


}
