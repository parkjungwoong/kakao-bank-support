package com.kakaopay.banksupport.common.csv;

import com.kakaopay.banksupport.dto.CsvBankSupportDTO;

import java.util.List;

public class LocalBankSupportMatcher extends CsvMatcher<CsvBankSupportDTO> {

    public LocalBankSupportMatcher() {
        super(CsvBankSupportDTO.class);
    }

    @Override
    protected boolean isMatch(List<String> columnNms) {
        return columnNms.size() == 9 &&
                columnNms.get(0).equals("구분") &&
                columnNms.get(1).equals("지자체명(기관명)") &&
                columnNms.get(2).equals("지원대상") &&
                columnNms.get(3).equals("용도") &&
                columnNms.get(4).equals("지원한도") &&
                columnNms.get(5).equals("이차보전") &&
                columnNms.get(6).equals("추천기관") &&
                columnNms.get(7).equals("관리점") &&
                columnNms.get(8).equals("취급점")
                ;
    }
}
