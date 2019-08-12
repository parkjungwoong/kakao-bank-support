package com.kakaopay.banksupport.common;

import com.kakaopay.banksupport.model.LocalBankSupport;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.Assert.*;

@Slf4j
public class CommonUtilsTest {

    @Test
    public void readCsvFile() {
        /*List<List<String>> lists = CommonUtils.readCsvFile("/Users/jungwoongpark/Documents/workspace/bank-support/src/main/resources/data/서버개발_사전과제1_지자체협약지원정보_16년12월현재__최종.csv");

        for(List<String> row : lists) {
            log.info("==============================");
            for(int i=0; row.size()>i; i++) {
                log.info("{}",row.get(i));
            }
            //sqlSession.insert();
        }*/
    }

    @Test
    public void name() throws Exception {
        Field[] declaredFields = LocalBankSupport.class.getDeclaredFields();

        LocalBankSupport ob = new LocalBankSupport();

        Field lbsId = ob.getClass().getDeclaredField("lbsId");
        lbsId.setAccessible(true);
        lbsId.set(lbsId,"asdf");

        log.info("lbsid => {}",ob.getLbsId());

        for(Field e : declaredFields) {
            log.info(e.getName());
        }
    }

    @Test
    public void name2() throws Exception {

        //todo: 매핑 해주는게 필요해, enum으로 가져와서
        LocalBankSupport book = new LocalBankSupport();
        Class<?> c = book.getClass();

        Field[] fields = c.getDeclaredFields();

        int i = 0;

        for(Field f : fields) {
            f.setAccessible(true);
            f.set(book,"fak"+i);
            i++;
        }

        /*Field chap = c.getDeclaredField("lbsId");
        chap.setAccessible(true);
        chap.set(book, "1234");*/

        log.info("id = > {}",book.getLbsId());
    }
}