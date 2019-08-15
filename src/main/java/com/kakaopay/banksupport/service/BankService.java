package com.kakaopay.banksupport.service;

import com.kakaopay.banksupport.common.CommonUtils;
import com.kakaopay.banksupport.common.constant.LbsSearchColum;
import com.kakaopay.banksupport.common.constant.ResCode;
import com.kakaopay.banksupport.common.constant.CsvGubunStr;
import com.kakaopay.banksupport.common.constant.WonUnit;
import com.kakaopay.banksupport.common.csv.LocalBankSupportMatcher;
import com.kakaopay.banksupport.common.exception.ComException;
import com.kakaopay.banksupport.config.ComSettings;
import com.kakaopay.banksupport.config.security.dto.UserContext;
import com.kakaopay.banksupport.dto.*;
import com.kakaopay.banksupport.dto.req.LbsSearchDTO;
import com.kakaopay.banksupport.dto.req.LbsUpdateDTO;
import com.kakaopay.banksupport.dto.res.ComDTO;
import com.kakaopay.banksupport.dto.res.LbsResDTO;
import com.kakaopay.banksupport.mapper.BankMapper;
import com.kakaopay.banksupport.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static com.kakaopay.banksupport.common.constant.BankConstant.APP_NAME;

@Slf4j
@Service
public class BankService {

    @Autowired private ComSettings comSettings;
    @Autowired private SqlSessionFactory sqlSessionFactory;
    @Autowired private BankMapper bankMapper;

    @Transactional
    public void setUpInitData() {
        //csv file read
        CsvDTO csvDTO = CommonUtils.readCsvFile(comSettings.getInitDataFilePath());

        //데이터 검사 및 변환
        List<CsvBankSupportDTO> converDataList = new LocalBankSupportMatcher().getConverData(csvDTO);

        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);

        //중복 검사용
        HashMap<String,String> regionHashMap = new HashMap<>();
        HashMap<String,String> usageCodeHashMap = new HashMap<>();

        //for
        for(CsvBankSupportDTO readData : converDataList) {

            //DTO 세팅
            RegionMas regionMas = null;
            List<UsageCode> usageCodeList = new ArrayList<>();
            List<UsageOpt> usageOptList = new ArrayList<>();
            LocalBankSupport localBankSupport;

            //========= RegionMas =========
            final String regionId;
            if(regionHashMap.containsKey(readData.getRegionNm())) {
                regionId = regionHashMap.get(readData.getRegionNm());
            } else {
                regionId = sqlSession.selectOne("getSeqRegionId");
                regionHashMap.put(readData.getRegionNm(), regionId);
                regionMas = RegionMas.builder()
                        .regionId(regionId)
                        .regionNm(readData.getRegionNm())
                        .build();
            }
            //========= /RegionMas =========

            final String lbsId = sqlSession.selectOne("getSeqLbsId");

            //========= usageCode & usageOpt =========
            String[] usageNms = usageStrToUsageArray(readData.getUsage());

            for(String usageNm : usageNms) {
                final String usageId;

                if(usageCodeHashMap.containsKey(usageNm)) {
                    usageId = usageCodeHashMap.get(usageNm);
                } else {
                    usageId = sqlSession.selectOne("getUsageCodeId");
                    usageCodeHashMap.put(usageNm, usageId);
                    usageCodeList.add(UsageCode.builder().usageCodeId(usageId).usage(usageNm).useYn("Y").build());
                }

                usageOptList.add(UsageOpt.builder().lbsId(lbsId).usageCodeId(usageId).useYn("Y").build());
            }
            //========= /usageCode & usageOpt=========

            //========= localBankSupport =========
            //지원한도 파싱
            long supportLimit = supportLimitStrToNumber(readData.getSupportLimit());

            //이차보전 파싱
            double[] rates = rateStrToDouble(readData.getRate());

            localBankSupport = LocalBankSupport.builder()
                    .lbsId(lbsId)
                    .regionId(regionId)
                    .target(readData.getTarget())
                    .supportLimit(supportLimit)
                    .rateMin(rates[0])
                    .rateAvg((rates[0]+rates[1])/2)
                    .rateMax(rates[1])
                    .institute(readData.getInstitute())
                    .mgmt(readData.getMgmt())
                    .reception(readData.getReception())
                    .instId(APP_NAME)
                    .build();
            //========= /localBankSupport =========
            //DTO 세팅 끝

            //배치 insert

            //지자체 원장 저장
            if(regionMas != null) {
                sqlSession.insert("insertRegion",regionMas);
            }

            //용도 코드 저장
            if(usageCodeList.size() > 0) {
                for(UsageCode usageCode : usageCodeList) {
                    sqlSession.insert("insertUsageCode",usageCode);
                }
            }

            //지원 정보 저장
            sqlSession.insert("insertBankSupport",localBankSupport);

            //용도 옵션 저장
            if(usageOptList.size() > 0) {
                for(UsageOpt usageOpt : usageOptList) {
                    sqlSession.insert("insertUsageOpt", usageOpt);
                }
            }

            //배치 insert 끝
        }
        //end for

        sqlSession.flushStatements();
        sqlSession.close();
    }

    private String[] usageStrToUsageArray(final String usage) {
        String[] split = usage.split(CsvGubunStr.USAGE_SPLIT.getStr());
        int len = split.length;

        for(int i=0; len>i; i++) {
            split[i] = split[i].trim();
        }

        return split;
    }

    /**
     * 지원한도 숫자로 변경
     * @param limitStr 지원한도 문자열
     * @return 지원한도 숫자
     */
    private long supportLimitStrToNumber(final String limitStr) {
        if(limitStr == null) return 0;

        if(!limitStr.equals(CsvGubunStr.SUPPORT_LIMIT.getStr())) {
            //한글 금액을 숫자로 변환
            return WonUnit.wonUnitToNumber(limitStr);
        } else {
            //추천금액 이내일 경우 -1으로 저장
            return -1;
        }
    }

    private String supportLimitNumberToStr(final Object limitNumber) {
        long pa = Long.parseLong(String.valueOf(limitNumber));
        if(pa == -1) return CsvGubunStr.SUPPORT_LIMIT.getStr();

        return WonUnit.numberToWonUnit(pa)+" 이내";
    }

    private double[] rateStrToDouble(final String rates) {
        if(rates == null) {
            log.error("이차 보전값 null");
            throw new ComException(ResCode.E013);
        }
                        //최소, 최대
        double[] result = {100, 100};

        if(!CsvGubunStr.RATES_100.getStr().equals(rates)) {
            String[] s = rates.split(CsvGubunStr.RATES_SPLIT.getStr());

            int rateCnt = s.length;

            for(int i=0; rateCnt>i; i++) {
                result[i] = CommonUtils.strPercentToDouble(s[i]);
            }

            if(rateCnt == 1) {
                for(int i=1; result.length>i; i++) {
                    result[i] = result[0];
                }
            }
        }

        return result;
    }

    private String doubleToRateStr(final LocalBankSupport dto) {
        double rateMin = dto.getRateMin();
        double rateMax = dto.getRateMax();

        if(rateMin >= 100.0) return CsvGubunStr.RATES_100.getStr();
        if(rateMin == rateMax) return rateMin + "%";
        return rateMin + "%" + CsvGubunStr.RATES_SPLIT.getStr() + rateMax + "%";
    }

    /**
     * 지원 원장 검색
     * @param searchDTO 검색 조건
     * @return 검색 결과
     */
    public LbsResDTO search(SearchDTO searchDTO) {

        List<String> sqlFields = getSqlFields(searchDTO);

        List<Map> resList = bankMapper.selectBankSupportMapList(
                LbsSearchDTO.builder()
                        .fields(sqlFields)
                        .match(getSqlMatcher(searchDTO))
                        .sort(getSqlSort(searchDTO))
                        .limit(searchDTO != null ? searchDTO.getLimit() : 0)
                        .build()
        );

        for(Map data : resList) {
            //데이터 표기 변형 작업
            if(data.containsKey(LbsSearchColum.SUPPORT_LIMIT.getColumNm())) {
                String wonStr = supportLimitNumberToStr(data.get(LbsSearchColum.SUPPORT_LIMIT.getColumNm()));
                data.put(LbsSearchColum.SUPPORT_LIMIT.getColumNm(), wonStr);
            }

            if(data.containsKey(LbsSearchColum.RATE.getColumNm())) {

                BigDecimal rateMax = (BigDecimal) data.get(LbsSearchColum.RATE_MAX.getColumNm());
                BigDecimal rateMin = (BigDecimal) data.get(LbsSearchColum.RATE_MIN.getColumNm());
                String rateStr = doubleToRateStr(LocalBankSupport.builder()
                        .rateMax(rateMax.doubleValue())
                        .rateMin(rateMin.doubleValue())
                        .build()
                );
                data.put(LbsSearchColum.RATE.name(), rateStr);
            }
        }

        LbsResDTO resDTO = new LbsResDTO();
        resDTO.setList(resList);
        resDTO.setResCode(ResCode.S000);

        return resDTO;
    }

    private List<String> getSqlFields(SearchDTO dto) {
        if(dto == null || dto.getFields() == null) return null;

        List<String> fList = new ArrayList<>(dto.getFields().size());

        List<String> reqFields = dto.getFields();
        //조회 컬럼값 설정
        for(String f : reqFields) {
            String columNm = LbsSearchColum.getColumNmByParamNm(f, false);
            fList.add(columNm);

            //RATE 컬럼시 MAX, MIN도 포함시켜서 반환
            if(LbsSearchColum.RATE.getColumNm().equals(columNm)) {
                fList.add(LbsSearchColum.RATE_MAX.getColumNm());
                fList.add(LbsSearchColum.RATE_MIN.getColumNm());
            }
        }

        return fList;
    }

    private String getSqlMatcher(SearchDTO dto) {
        if(dto == null || dto.getMatch() == null) return "";

        Set<String> matchs = dto.getMatch().keySet();

        Iterator<String> iterator = matchs.iterator();

        StringBuilder sb = new StringBuilder();

        while(iterator.hasNext()) {
            String key = iterator.next();
            //필드값 존재 여부 검사
            String columNm = LbsSearchColum.getColumNmByParamNm(key, true);
            //id='userId' AND pw='aaa'
            sb.append(columNm).append("='").append(dto.getMatch().get(key)).append("' AND ");
        }

        return sb.substring(0,sb.length()-5);
    }

    private String getSqlSort(SearchDTO dto) {
        if(dto == null || dto.getSort() == null) return "";
        List<Map<String, String>> sortList = dto.getSort();

        StringBuilder sb = new StringBuilder();

        for(Map<String, String> sort : sortList) {
            Set<String> sorts = sort.keySet();

            for (String key : sorts) {
                //필드값 존재 여부 검사
                String columNm = LbsSearchColum.getColumNmByParamNm(key, true);
                String sortOpt = StringUtils.isBlank(sort.get(key)) ? "ASC" : "DESC";

                sb.append(columNm).append(" ").append(sortOpt).append(",");
            }
        }

        return sb.substring(0,sb.length()-1);
    }

    /**
     * 지원 원장 변경
     * @param dto 변경 정보
     * @return 공통 응답
     */
    @Transactional
    public ComDTO update(LbsUpdateDTO dto, UserContext userContext) {
        LocalBankSupport localBankSupport = new LocalBankSupport();

        BeanUtils.copyProperties(dto, localBankSupport);

        //지원한도 파싱
        if(dto.getSupportLimit() != null) {
            long supportLimit = supportLimitStrToNumber(dto.getSupportLimit());
            localBankSupport.setSupportLimit(supportLimit);
        }

        //이차보전 파싱
        if(dto.getRate() != null) {
            double[] rates = rateStrToDouble(dto.getRate());
            localBankSupport.setRateMin(rates[0]);
            localBankSupport.setRateAvg((rates[0]+rates[1])/2);
            localBankSupport.setRateMax(rates[1]);
        }

        //수정자 정보
        localBankSupport.setUpdtId(userContext.getUserNo());

        int updateCnt = bankMapper.updateLocalBankSupport(localBankSupport);
        ResCode resCode = updateCnt > 0 ? ResCode.S000 : ResCode.E012;

        return new ComDTO(resCode);
    }
}
