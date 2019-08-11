package com.kakaopay.banksupport.service;

import com.kakaopay.banksupport.common.CommonUtils;
import com.kakaopay.banksupport.common.csv.LocalBankSupportMatcher;
import com.kakaopay.banksupport.config.ComSettings;
import com.kakaopay.banksupport.dto.CsvBankSupportDTO;
import com.kakaopay.banksupport.dto.CsvDTO;
import com.kakaopay.banksupport.mapper.BankMapper;
import com.kakaopay.banksupport.model.LocalBankSupport;
import com.kakaopay.banksupport.model.LocalBankSupportDtl;
import com.kakaopay.banksupport.model.RegionMas;
import com.kakaopay.banksupport.model.UsageCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class BankService {

    @Autowired private ComSettings comSettings;
    @Autowired private SqlSessionFactory sqlSessionFactory;
    @Autowired private BankMapper bankMapper;

    @Transactional
    public void setUpInitData() {
        log.info("초기 데이터 로드 시작 ");

        //csv file read
        CsvDTO csvDTO = CommonUtils.readCsvFile(comSettings.getInitDataFilePath());

        //데이터 검사 및 변환
        List<CsvBankSupportDTO> converDataList = new LocalBankSupportMatcher().getConverData(csvDTO);

        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);

        HashMap<String,String> regionMap = new HashMap<>();
        HashMap<String,String> usageCodeMap = new HashMap<>();

        for(CsvBankSupportDTO readData : converDataList) {

            //기관 정보 아이디 생성
            String regionId = regionMap.get(readData.getRegionNm());
            if(StringUtils.isEmpty(regionId)) {
                regionId = bankMapper.getSeqRegionId();
                regionMap.put(readData.getRegionNm(), regionId);
            }

            //기관 정보 저장
            bankMapper.insertRegion(
                    RegionMas.builder()
                            .regionId(regionId)
                            .name(readData.getRegionNm())
                    .build()
            );

            //지원 정보 저장
            sqlSession.insert("insertBankSupport",
                    LocalBankSupport.builder()
                            .regionId(regionId)
                            .target(readData.getTarget())
                            .usage(readData.getUsage())
                            .supportLimit(readData.getSupportLimit())
                            .rate(readData.getRate())
                            .institute(readData.getInstitute())
                            .mgmt(readData.getMgmt())
                            .reception(readData.getReception())
                    .build()
            );

            //todo: 용도 구분 파싱
            String[] usages = readData.getUsage().split("및");
            for(String usa : usages) {
                String usageCodeId = usageCodeMap.get(usa.trim());

                //중복 검사
                if(StringUtils.isEmpty(usageCodeId)) {
                    usageCodeId = bankMapper.getUsageCodeId();
                    usageCodeMap.put(usa.trim(), usageCodeId);
                }

                //용도 코드 저장
                bankMapper.insertUsageCode(
                        UsageCode.builder()
                                .usageCodeId(usageCodeId)
                                .val(usa.trim())
                                .useYn("Y")
                        .build()
                );
            }

            //todo: 지원한도 파싱
            String limitStr = readData.getSupportLimit();

            long supportLimit = 0;
            if(!limitStr.equals("추천금액 이내")) {
                //한글 금액을 숫자로 변환
                supportLimit = CommonUtils.wonUnitToNumber(limitStr);
            }

            //todo: 이차보전 파싱
            float min=0;
            float max=0;

            String[] rates = readData.getRate().split("~");
            //소수점 두자리 까지
            for(int i=0; rates.length>i; i++) {
                float a = CommonUtils.strPercentToFloat(rates[i]);
                if(i == 0) min = a;
                if(i == 1) max = a;
            }

            //todo: 지원 정보 상세 저장
            bankMapper.insertLocalBankSupportDtl(
                    LocalBankSupportDtl.builder()
                            .lbsId(regionId)
                            .supportLimit(supportLimit)
                            .rateMin(min)
                            .rateAvg((max+min)/2)
                            .rateMax(max)
                            .build()
            );

            //todo: 용도 옵션 저장

        }

    }
}
