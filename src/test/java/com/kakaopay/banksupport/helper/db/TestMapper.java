package com.kakaopay.banksupport.helper.db;

import com.kakaopay.banksupport.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface TestMapper {

    @SelectProvider(type = TestSql.class, method = "지원정보_원장_전체")
    List<LocalBankSupport> 지원정보_원장_전체();

    @SelectProvider(type = TestSql.class, method = "지원_정보_상세_전체")
    List<LocalBankSupportDtl> 지원_정보_상세_전체();

    @SelectProvider(type = TestSql.class, method = "용도_코드_전체")
    List<UsageCode> 용도_코드_전체();

    @SelectProvider(type = TestSql.class, method = "용도_옵션_전체")
    List<UsageOpt> 용도_옵션_전체();

    @SelectProvider(type = TestSql.class, method = "지자체_정보_전체")
    List<RegionMas> 지자체_정보_전체();

    @SelectProvider(type = TestSql.class, method = "지역_정보_전체")
    List<RegionMap> 지역_정보_전체();


}
