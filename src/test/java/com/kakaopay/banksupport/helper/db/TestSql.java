package com.kakaopay.banksupport.helper.db;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class TestSql {

    public static String 지원정보_원장_전체() {
        return new SQL(){{
            SELECT("*");
            FROM("LOCAL_BANK_SUPPORT");
        }}.toString();
    }

    public static String 지원_정보_상세_전체() {
        return new SQL(){{
            SELECT("*");
            FROM("LOCAL_BANK_SUPPORT_DTL");
        }}.toString();
    }

    public static String 용도_코드_전체() {
        return new SQL(){{
            SELECT("*");
            FROM("USAGE_CODE");
        }}.toString();
    }

    public static String 용도_옵션_전체() {
        return new SQL(){{
            SELECT("*");
            FROM("USAGE_OPT");
        }}.toString();
    }

    public static String 지자체_정보_전체() {
        return new SQL(){{
            SELECT("*");
            FROM("REGION_MAS");
        }}.toString();
    }

    public static String 지역_정보_전체() {
        return new SQL(){{
            SELECT("*");
            FROM("REGION_MAP");
        }}.toString();
    }


    //todo: 회원쪽 개발할때 다시
    public static String 사용자_정보_전체() {
        return new SQL(){{
            SELECT("*");
            FROM("USER_INFO");
        }}.toString();
    }
}
