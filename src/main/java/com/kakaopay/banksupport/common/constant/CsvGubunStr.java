package com.kakaopay.banksupport.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CsvGubunStr {
    USAGE_SPLIT("및"),
    SUPPORT_LIMIT("추천금액 이내"),
    RATES_SPLIT("~"),
    RATES_100("대출이자 전액")
    ;

    private String str;
}
