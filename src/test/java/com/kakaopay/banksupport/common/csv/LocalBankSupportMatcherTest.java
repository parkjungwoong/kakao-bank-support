package com.kakaopay.banksupport.common.csv;

import com.kakaopay.banksupport.common.constant.ResCode;
import com.kakaopay.banksupport.common.exception.ComException;
import com.kakaopay.banksupport.dto.CsvBankSupportDTO;
import com.kakaopay.banksupport.dto.CsvDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@Slf4j
public class LocalBankSupportMatcherTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private LocalBankSupportMatcher lbsMatcher = new LocalBankSupportMatcher();

    @Test
    public void CSV파일의_내용이_DTO로변환될_데이터가_맞는지_검사() {
        /*--------------- given ---------------*/
        List<String> matchColum = getFakeData().get(0);
        List<String> notMatchColum = Arrays.asList("1","2","3");

        /*--------------- when ---------------*/
        boolean match = lbsMatcher.isMatch(matchColum);
        boolean notMatch = lbsMatcher.isMatch(notMatchColum);

        /*--------------- then ---------------*/
        assertThat(match, is(true));
        assertThat(notMatch, is(false));
    }

    @Test
    public void CsvBankSupportDTO객체로_변환() {
        /*--------------- given ---------------*/
        CsvDTO csvDTO = CsvDTO.CreateCsvDTO(getFakeData());

        /*--------------- when ---------------*/
        List<CsvBankSupportDTO> converData = lbsMatcher.getConverData(csvDTO);

        /*--------------- then ---------------*/
        assertThat(converData, notNullValue());
        assertThat(converData.size(), is(csvDTO.getRow().size()-1)); //컬럼명 있는 row는 제외
        assertThat(converData.get(0).getGubun(),        is(csvDTO.getRowData(1).get(0))); //구분
        assertThat(converData.get(0).getRegionNm(),     is(csvDTO.getRowData(1).get(1))); //지차체명
        assertThat(converData.get(0).getTarget(),       is(csvDTO.getRowData(1).get(2))); //지원대상
        assertThat(converData.get(0).getUsage(),        is(csvDTO.getRowData(1).get(3))); //용도
        assertThat(converData.get(0).getSupportLimit(), is(csvDTO.getRowData(1).get(4))); //지원한도
        assertThat(converData.get(0).getRate(),         is(csvDTO.getRowData(1).get(5))); //이차보전
        assertThat(converData.get(0).getInstitute(),    is(csvDTO.getRowData(1).get(6))); //추천기관
        assertThat(converData.get(0).getMgmt(),         is(csvDTO.getRowData(1).get(7))); //관리점
        assertThat(converData.get(0).getReception(),    is(csvDTO.getRowData(1).get(8))); //취급점
    }

    @Test
    public void CsvBankSupportDTO객체로_변환_실패() {
        expectedException.expect(ComException.class);
        expectedException.expectMessage(ResCode.E001.getMessage());

        /*--------------- given ---------------*/
        List<List<String>> fakeData = getFakeData();
        fakeData.set(0, Arrays.asList("1","2","3"));

        CsvDTO csvDTO = CsvDTO.CreateCsvDTO(fakeData);

        /*--------------- when ---------------*/
        lbsMatcher.getConverData(csvDTO);

        /*--------------- then ---------------*/
        //Exception
    }

    private List<List<String>> getFakeData() {
        List<List<String>> list = new ArrayList<>();
        list.add(Arrays.asList("구분","지자체명(기관명)","지원대상","용도","지원한도","이차보전","추천기관","관리점","취급점"));
        list.add(Arrays.asList("1","2","3","4","5","6","7","8","9"));
        list.add(Arrays.asList("10","11","12","13","14","15","16","17","18"));

        return list;
    }
}