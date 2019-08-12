package com.kakaopay.banksupport.service;

import com.kakaopay.banksupport.common.CommonUtils;
import com.kakaopay.banksupport.config.ComSettings;
import com.kakaopay.banksupport.dto.CsvDTO;
import com.kakaopay.banksupport.helper.db.TestMapper;
import com.kakaopay.banksupport.mapper.BankMapper;
import com.kakaopay.banksupport.model.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class BankServiceTest {

    @Autowired private BankService service;
    @Autowired private ComSettings comSettings;
    @Autowired private TestMapper testMapper;

    private static final String testCsvFilePath = "src/test/testData.csv";
    private static CsvDTO testCsvData;

    @Before
    public void before() {
        /*--------------- given ---------------*/
        comSettings.setInitDataFilePath(testCsvFilePath); //해당 설정값 경로의 csv 파일을 읽음
        testCsvData = CommonUtils.readCsvFile(testCsvFilePath);

        /*--------------- when ---------------*/
        service.setUpInitData();
    }

    @Test
    public void 지원_정보_원장_데이터_검사() {
        List<List<String>> row = testCsvData.getRow();
        /*--------------- then ---------------*/
        List<LocalBankSupport> 지원정보_원장_전체 = testMapper.지원정보_원장_전체();

        assertThat(지원정보_원장_전체.size(), is(4));
        for(LocalBankSupport lbs : 지원정보_원장_전체) {
            assertThat(lbs.getLbsId(), notNullValue());
        }
    }

    @Test
    public void 기준정보_데이터_세팅() {
        /*--------------- then ---------------*/
        List<LocalBankSupport> 지원정보_원장_전체 = testMapper.지원정보_원장_전체();
        assertThat(지원정보_원장_전체.size(), is(4));

        List<LocalBankSupportDtl> 지원_정보_상세_전체 = testMapper.지원_정보_상세_전체();
        assertThat(지원_정보_상세_전체.size(), is(4));

        //용도 코드
        List<UsageCode> 용도_코드_전체 = testMapper.용도_코드_전체();
        assertThat(용도_코드_전체.size(), is(2));

        List<UsageOpt> 용도_옵션_전체 = testMapper.용도_옵션_전체();
        assertThat(용도_옵션_전체.size(), is(6));

        //지자체 정보
        List<RegionMas> 지자체_정보_전체 = testMapper.지자체_정보_전체();
        assertThat(지자체_정보_전체.size(), is(4));

        //todo: 지역정보 개발 완료되면 테스트 넣기
        List<RegionMap> 지역_정보_전체 = testMapper.지역_정보_전체();
    }
}