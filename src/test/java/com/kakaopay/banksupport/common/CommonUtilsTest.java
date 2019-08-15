package com.kakaopay.banksupport.common;

import com.kakaopay.banksupport.common.constant.ResCode;
import com.kakaopay.banksupport.common.exception.ComException;
import com.kakaopay.banksupport.dto.CsvDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

@Slf4j
public class CommonUtilsTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private final String TEST_CSV_FILE_PATH = "src/test/testData.csv";
    private final int TEST_CSV_ROW_CNT = 6;

    @Test
    public void CSV파일읽기() {
        /*--------------- given ---------------*/
        //TEST_CSV_FILE_PATH 파일 경로

        /*--------------- when ---------------*/
        CsvDTO csvDTO = CommonUtils.readCsvFile(TEST_CSV_FILE_PATH);

        /*--------------- then ---------------*/
        assertThat(csvDTO, notNullValue());
        assertThat(csvDTO.getRow().size(), is(TEST_CSV_ROW_CNT));
    }

    @Test
    public void CSV파일읽기_파일없을때() {
        expectedException.expect(ComException.class);
        expectedException.expectMessage(ResCode.E999.getMessage());

        /*--------------- given ---------------*/
        final String fileNotfound = "";

        /*--------------- when ---------------*/
        CommonUtils.readCsvFile(fileNotfound);

        /*--------------- then ---------------*/
        //Exception E999
    }

    @Test
    public void 쉼표로_구분된_문자열_파싱() {
        /*--------------- given ---------------*/
        final String row = "가,a,1,$,\"따옴표, 포함된내용\"";

        /*--------------- when ---------------*/
        List<String> splitedRow = CommonUtils.parsingCsvRow(row);

        /*--------------- then ---------------*/
        assertThat(splitedRow.get(0), is("가"));
        assertThat(splitedRow.get(1), is("a"));
        assertThat(splitedRow.get(2), is("1"));
        assertThat(splitedRow.get(3), is("$"));
        assertThat(splitedRow.get(4), is("따옴표, 포함된내용"));
    }

    @Test
    public void 쉼표로_구분된_문자열_파싱_null값처리() {
        /*--------------- given ---------------*/
        final String row = null;

        /*--------------- when ---------------*/
        List<String> splitedRow = CommonUtils.parsingCsvRow(row);

        /*--------------- then ---------------*/
        assertThat(splitedRow, notNullValue());
        assertThat(splitedRow.size(), is(0));
    }

    @Test
    public void 쉼표로_구분된_문자열_파싱_공백값처리() {
        /*--------------- given ---------------*/
        final String rowEmpty = "";
        final String rowSpace = "  ";

        /*--------------- when ---------------*/
        List<String> splitedRowEmpty = CommonUtils.parsingCsvRow(rowEmpty);
        List<String> splitedRowSpace = CommonUtils.parsingCsvRow(rowSpace);

        /*--------------- then ---------------*/
        assertThat(splitedRowEmpty, notNullValue());
        assertThat(splitedRowEmpty.size(), is(0));

        assertThat(splitedRowSpace, notNullValue());
        assertThat(splitedRowSpace.size(), is(0));
    }


}