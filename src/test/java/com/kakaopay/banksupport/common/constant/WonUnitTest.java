package com.kakaopay.banksupport.common.constant;

import com.kakaopay.banksupport.common.exception.ComException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class WonUnitTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void 한글_금액_단위_숫자로변환() {
        /*--------------- given ---------------*/
        final String 억단위 = "30억원";
        final String 천만단위 = "1천만원";
        final String 백만단위 = "3백만원";

        /*--------------- when ---------------*/
        long 삼십억원 = WonUnit.wonUnitToNumber(억단위);
        long 일천만원 = WonUnit.wonUnitToNumber(천만단위);
        long 삼백만원 = WonUnit.wonUnitToNumber(백만단위);

        /*--------------- then ---------------*/
        assertThat(삼십억원, is(3000000000L));
        assertThat(일천만원, is(10000000L));
        assertThat(삼백만원, is(3000000L));
    }

    @Test
    public void 한글_금액_단위_숫자변환_실패() {
        expectedException.expect(ComException.class);
        expectedException.expectMessage(ResCode.E002.getMessage());

        /*--------------- given ---------------*/
        final String 변환실패숫자 = "일천만원"; //숫자 + 한글 단위만 허용

        /*--------------- when ---------------*/
        WonUnit.wonUnitToNumber(변환실패숫자);

        /*--------------- then ---------------*/
        //Exception E002
    }

    @Test
    public void 숫자_금액을_한글_단위로_변환() {
        /*--------------- given ---------------*/
        final long 삼십억원 = 3000000000L;
        final long 일천만원 = 10000000L;
        final long 삼백만원 = 3000000L;

        /*--------------- when ---------------*/
        String 억단위  = WonUnit.numberToWonUnit(삼십억원);
        String 천만단위 = WonUnit.numberToWonUnit(일천만원);
        String 백만단위 = WonUnit.numberToWonUnit(삼백만원);

        /*--------------- then ---------------*/
        assertThat(억단위, is("30억원"));
        assertThat(천만단위, is("1천만원"));
        assertThat(백만단위, is("3백만원"));
    }
}