package com.kakaopay.banksupport.common.constant;

import com.kakaopay.banksupport.common.exception.ComException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Getter
public enum LbsSearchColum {

    LBS_ID("lbsId","LBS_ID","T1"),
    REGION_ID("regionId","REGION_ID","T1"),
    REGION_NM("regionNm","REGION_NM","T2"),
    TARGET("target","TARGET","T1"),
    USAGE("usage","USAGE","T4"),
    SUPPORT_LIMIT("supportLimit","SUPPORT_LIMIT","T1"),
    RATE("rate","RATE_AVG","T1"),
    RATE_MIN("rateMin","RATE_MIN","T1"),
    RATE_MAX("rateMax","RATE_MAX","T1"),
    INSTITUTE("institute","INSTITUTE","T1"),
    MGMT("mgmt","MGMT","T1"),
    RECEPTION("reception","RECEPTION","T1")
    ;

    private String paramNm;
    private String columNm;
    private String tableAlias;

    /**
     * 검색 요청 파라미터 값으로 컬럼명 반환
     * @param paramNm 검색 요청 파라미터
     * @return 컬럼명 반환
     * @throws ComException 일치하는 컬럼 없을 경우 발생
     */
    public static String getColumNmByParamNm(String paramNm, boolean tableAlias) throws ComException {
        for(LbsSearchColum lsc : LbsSearchColum.values()) {
            if(lsc.paramNm.equals(paramNm)) {
                return tableAlias ? lsc.getTableAlias()+"."+lsc.getColumNm() : lsc.getColumNm();
            }
        }

        log.error("paramNm : {} no match columNm ", paramNm);
        throw new ComException(ResCode.E010);
    }
}


