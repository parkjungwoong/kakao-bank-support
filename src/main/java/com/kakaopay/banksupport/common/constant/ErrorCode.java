package com.kakaopay.banksupport.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    E001("처리 가능한 csv파일이 아닙니다."),



    E999("내부 오류")
    ;

    private String message;
}
