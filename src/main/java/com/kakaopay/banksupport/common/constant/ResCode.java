package com.kakaopay.banksupport.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResCode {
    S000("정상처리되었습니다."),

    E001("처리 가능한 csv파일이 아닙니다."),
    E002("한글 원화 표기 변환 오류(숫자+한글단위 형식만 허용)."),
    E003("허용되지 않은 http 메소드."),
    E004("계정 정보를 불일치."),
    E005("파라미터값 누락."),
    E006("토큰 생성 오류."),
    E007("토큰 파싱 오류."),
    E008("토큰 만료."),
    E009("인증 정보 누락."),
    E010("요청 값 확인 필요."),
    E011("이미 가입된 회원."),
    E012("일치하는 정보 없음."),
    E013("파리미터 유효성 오류."),
    E014("퍼센트 표기 변환 오류(숫자+% 형식만 허용)."),

    E999("내부 오류")
    ;

    private String message;
}
