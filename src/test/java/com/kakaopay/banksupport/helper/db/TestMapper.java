package com.kakaopay.banksupport.helper.db;

import com.kakaopay.banksupport.model.*;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface TestMapper {

    @SelectProvider(type = TestSql.class, method = "지원정보_원장_전체")
    List<LocalBankSupport> 지원정보_원장_전체();

    @SelectProvider(type = TestSql.class, method = "용도_코드_전체")
    List<UsageCode> 용도_코드_전체();

    @SelectProvider(type = TestSql.class, method = "용도_옵션_전체")
    List<UsageOpt> 용도_옵션_전체();

    @SelectProvider(type = TestSql.class, method = "지자체_정보_전체")
    List<RegionMas> 지자체_정보_전체();

    @SelectProvider(type = TestSql.class, method = "지역_정보_전체")
    List<RegionMap> 지역_정보_전체();

    @SelectProvider(type = TestSql.class, method = "쿼리_문자열로")
    List<LocalBankSupport> 지원정보_원장_검색_쿼리로(String sql);

    @SelectProvider(type = TestSql.class, method = "쿼리_문자열로")
    List<UserInfo> 회원정보_검색_쿼리로(String sql);

    @DeleteProvider(type = TestSql.class, method = "쿼리_문자열로")
    void 삭제_쿼리(String sql);
}
