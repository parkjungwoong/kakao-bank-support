package com.kakaopay.banksupport.mapper;

import com.kakaopay.banksupport.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface BankMapper {

    @Select("SELECT TO_CHAR(SYSDATE,'YYYYMMDD')||SEQ_REGION_ID.NEXTVAL FROM DUAL")
    String getSeqRegionId();

    void insertBankSupport(LocalBankSupport localBankSupport);

    List<LocalBankSupport> selectBankSupport(LocalBankSupport localBankSupport);

    UserInfo selectUserById();

    void insertRegion(RegionMas build);

    //용도 코드 저장
    void insertUsageCode(UsageCode usageCode);

    //용도 옵션 저장
    void insertUsageOpt(UsageOpt usageOpt);
}
