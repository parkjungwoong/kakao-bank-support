package com.kakaopay.banksupport.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    E001("처리 가능한 csv파일이 아닙니다."),
    E002("한글 원화 표기 변환 오류"),
    E003("허용되지 않은 http 메소드."),
    E004("계정 정보를 불일치."),
    E005("파라미터값 누락."),
    E006("토큰 생성 오류."),
    E007("토큰 파싱 오류."),
    E008("토큰 만료."),

    E999("내부 오류")
    ;

    private String message;
}
