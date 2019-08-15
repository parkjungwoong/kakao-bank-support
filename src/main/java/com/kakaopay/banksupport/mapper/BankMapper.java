package com.kakaopay.banksupport.mapper;

import com.kakaopay.banksupport.dto.req.LbsSearchDTO;
import com.kakaopay.banksupport.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface BankMapper {

    //지원정보 아이디
    @Select("SELECT TO_CHAR(SYSDATE,'YYYYMMDD')||SEQ_LBS_ID.NEXTVAL FROM DUAL")
    String getSeqLbsId();

    //기관 아이디
    @Select("SELECT TO_CHAR(SYSDATE,'YYYYMMDD')||SEQ_REGION_ID.NEXTVAL FROM DUAL")
    String getSeqRegionId();

    void insertBankSupport(LocalBankSupport localBankSupport);

    List<LocalBankSupport> selectBankSupport(LocalBankSupport localBankSupport);

    void insertRegion(RegionMas build);

    //=== 용도 관련 ===
    @Select("SELECT TO_CHAR(SYSDATE,'YYYYMMDD')||SEQ_USAGE_CODE_ID.NEXTVAL FROM DUAL")
    String getUsageCodeId();

    //용도 코드 저장
    void insertUsageCode(UsageCode usageCode);

    //용도 옵션 저장
    void insertUsageOpt(UsageOpt usageOpt);

    @SelectProvider(type = BankMapper.class, method = "dynamicSearch")
    List<LocalBankSupport> selectBankSupportDynamic(SQL sql);

    public static String dynamicSearch(SQL sql) {
        return sql.toString();
    }

    int updateLocalBankSupport(LocalBankSupport localBankSupport);

    List<LocalBankSupport> selectBankSupportList(LbsSearchDTO dto);

    List<Map> selectBankSupportMapList(LbsSearchDTO dto);
}
