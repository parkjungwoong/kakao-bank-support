package com.kakaopay.banksupport.common.csv;

import com.kakaopay.banksupport.dto.CsvBankSupportDTO;

public class LocalBankSupportMatcher extends CsvMatcher<CsvBankSupportDTO> {

    public LocalBankSupportMatcher() {
        super(CsvBankSupportDTO.class);
    }

    @Override
    protected boolean isMatch(String[] columnNms) {
        return columnNms.length == 9 ||
                columnNms[0].equals("구분") ||
                columnNms[1].equals("지자체명(기관명)") ||
                columnNms[2].equals("지원대상") ||
                columnNms[3].equals("용도") ||
                columnNms[4].equals("지원한도") ||
                columnNms[5].equals("이차보전") ||
                columnNms[6].equals("추천기관") ||
                columnNms[7].equals("관리점") ||
                columnNms[8].equals("취급점")
                ;
    }
}
