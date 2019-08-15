package com.kakaopay.banksupport.dto;

import com.kakaopay.banksupport.common.csv.CsvMatcherDTO;
import lombok.Data;

@Data
public class CsvBankSupportDTO implements CsvMatcherDTO {
    private String gubun;
    private String regionNm;
    private String target;
    private String usage;
    private String supportLimit;
    private String rate;
    private String institute; //추천기관
    private String mgmt;//관리점
    private String reception;//취급점
}
