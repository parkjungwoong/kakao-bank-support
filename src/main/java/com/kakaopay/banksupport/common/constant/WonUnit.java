package com.kakaopay.banksupport.common.constant;

import com.kakaopay.banksupport.common.exception.ComException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
public enum  WonUnit {
    //!! 큰 단위부터 위에서 나열
    EOG_WON("억원",8),
    CHEON_MAN_WON("천만원",7),
    BAEG_MAN_WON("백만원",6)
    ;

    private String korUnit;
    private int zeroCnt;

    public long getZeroCnt() {
        return (long) Math.pow(10,zeroCnt);
    }

    /**
     * 한글 원화 표기를 숫자로 변환
     * @param wonStr 한글 원화
     * @return 숫자
     * @throws ComException 변환 가능한 포멧이 아닐때 ErrorCode.E002
     */
    public static long wonUnitToNumber(final String wonStr) throws ComException {
        for(WonUnit wonUnit : WonUnit.values()) {
            String[] strings = wonStr.trim().split(wonUnit.getKorUnit());

            if(StringUtils.isNumeric(strings[0])) {
                return Long.parseLong(strings[0]) * wonUnit.getZeroCnt();
            }
        }

        throw new ComException(ResCode.E002);
    }

    public static String numberToWonUnit(final long won) throws ComException {
        for(WonUnit wonUnit : WonUnit.values()) {
            long unit = wonUnit.getZeroCnt();
            if(won/unit > 0) {
                return (won/unit) + wonUnit.korUnit;
            }
        }

        throw new ComException(ResCode.E002);
    }
}
