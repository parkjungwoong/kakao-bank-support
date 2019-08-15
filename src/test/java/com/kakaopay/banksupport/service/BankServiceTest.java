package com.kakaopay.banksupport.service;

import com.kakaopay.banksupport.common.CommonUtils;
import com.kakaopay.banksupport.config.ComSettings;
import com.kakaopay.banksupport.dto.CsvDTO;
import com.kakaopay.banksupport.dto.SearchDTO;
import com.kakaopay.banksupport.dto.res.LbsResDTO;
import com.kakaopay.banksupport.helper.db.TestMapper;
import com.kakaopay.banksupport.model.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        //comSettings.setInitDataFilePath(testCsvFilePath); //해당 설정값 경로의 csv 파일을 읽음
        //testCsvData = CommonUtils.readCsvFile(testCsvFilePath);

        /*--------------- when ---------------*/
        //service.setUpInitData();
    }

    @Test
    public void 지원_정보_원장_데이터_검사() {
        /*--------------- then ---------------*/
        List<LocalBankSupport> 지원정보_원장_전체 = testMapper.지원정보_원장_전체();

        for(LocalBankSupport lbs : 지원정보_원장_전체) {
            assertThat(lbs.getLbsId(), notNullValue());
        }
    }

    @Test
    public void 검색옵션이_없으며_전체_검색() {
        /*--------------- given ---------------*/
        SearchDTO searchDTO = new SearchDTO();

        /*--------------- when ---------------*/
        LbsResDTO search = service.search(searchDTO);

        /*--------------- then ---------------*/
        assertThat(search.getList(), notNullValue());
    }
}