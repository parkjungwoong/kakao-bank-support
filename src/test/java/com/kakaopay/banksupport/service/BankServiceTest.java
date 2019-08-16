package com.kakaopay.banksupport.service;

import com.kakaopay.banksupport.common.constant.CsvGubunStr;
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

import java.util.List;
import org.powermock.reflect.Whitebox;

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

    @Test
    public void 용도_파싱() throws Exception {
        /*--------------- given ---------------*/
        String usages = "가 및 나 및 다";

        /*--------------- when ---------------*/
        String[] o = Whitebox.invokeMethod(service, "usageStrToUsageArray", "가 및 나 및 다");

        /*--------------- then ---------------*/
        assertThat(o.length, is(3));
        assertThat(o[0], is("가"));
        assertThat(o[1], is("나"));
        assertThat(o[2], is("다"));
    }

    @Test
    public void 지원금액_파싱() throws Exception {
        /*--------------- given ---------------*/
        String supportLImit = "3백만원";

        /*--------------- when ---------------*/
        long r1 = Whitebox.invokeMethod(service, "supportLimitStrToNumber", CsvGubunStr.SUPPORT_LIMIT.getStr());
        long r2 = Whitebox.invokeMethod(service, "supportLimitStrToNumber", supportLImit);

        /*--------------- then ---------------*/
        assertThat(r1, is(-1L));
        assertThat(r2, is(3000000L));
    }

    @Test
    public void 지원금액_파싱_역으로() throws Exception {
        /*--------------- given ---------------*/
        String supportLImit = "3000000";
        String supportLImit2 = "-1";

        /*--------------- when ---------------*/
        String r1 = Whitebox.invokeMethod(service, "supportLimitNumberToStr", supportLImit);
        String r2 = Whitebox.invokeMethod(service, "supportLimitNumberToStr", supportLImit2);

        /*--------------- then ---------------*/
        assertThat(r1, is("3백만원 이내"));
        assertThat(r2, is(CsvGubunStr.SUPPORT_LIMIT.getStr()));
    }

    @Test
    public void 이차보전값_파싱() throws Exception {
        /*--------------- given ---------------*/
        String supportLImit = "1% ~ 3%";
        String supportLImit2 = "1%";

        /*--------------- when ---------------*/
        double[] r1 = Whitebox.invokeMethod(service, "rateStrToDouble", supportLImit);
        double[] r2 = Whitebox.invokeMethod(service, "rateStrToDouble", supportLImit2);

        /*--------------- then ---------------*/
        assertThat(r1.length, is(2));
        assertThat(r2.length, is(2));

        assertThat(r1[0], is(1.0));
        assertThat(r1[1], is(3.0));

        assertThat(r2[0], is(1.0));
        assertThat(r2[1], is(1.0));
    }

    @Test
    public void 이차보전값_파싱_역으로() throws Exception {
        /*--------------- given ---------------*/
        LocalBankSupport p1 = new LocalBankSupport();
        p1.setRateMin(1.0);
        p1.setRateMax(3.0);

        LocalBankSupport p2 = new LocalBankSupport();
        p2.setRateMin(1.0);
        p2.setRateMax(1.0);

        /*--------------- when ---------------*/
        String r1 = Whitebox.invokeMethod(service, "doubleToRateStr", p1);
        String r2 = Whitebox.invokeMethod(service, "doubleToRateStr", p2);

        /*--------------- then ---------------*/
        assertThat(r1, is("1.0%~3.0%"));
        assertThat(r2, is("1.0%"));
    }
}