package com.kakaopay.banksupport.common.csv;

import com.kakaopay.banksupport.common.constant.ErrorCode;
import com.kakaopay.banksupport.common.exception.ComException;
import com.kakaopay.banksupport.dto.CsvDTO;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Csv파일 검사 및 데이터 변환
 * @param <T> 변환할 데이터 타입
 */
@Slf4j
public abstract class CsvMatcher<T> {

    private Class<T> clazzOfT;

    public CsvMatcher(Class<T> clazzOfT) {
        this.clazzOfT = clazzOfT;
    }

    /**
     * 변환할 데이터가 맞는지 검사
     * 컬럼명, 컬럼 개수 확인, 등의 작업 작성
     * @param columnNms csv파일의 첫번째 row
     * @return true면 변환 대상임
     */
    abstract protected boolean isMatch(List<String> columnNms);

    /**
     * CsvDTO -> 변환할 데이터 타입
     * @param csvDTO csv파일에서 읽은 데이터
     * @return 변환된 데이터
     * @throws ComException
     */
    public List<T> getConverData(CsvDTO csvDTO) throws ComException {
        //todo: null 및 데이터 길이 검사

        boolean match = isMatch(csvDTO.getRowData(0));
        if(!match) throw new ComException(ErrorCode.E001);

        List<T> result = new ArrayList<>(csvDTO.getRow().size());

        for(int i=1; csvDTO.getRow().size()>i; i++) {
            result.add(getConvertData(csvDTO.getRowData(i)));
        }

        return result;
    }

    private T getConvertData(List<String> rowData) {
        //todo: 변수명 및 에러 처리 수정하기
        T t;

        try {
            t = clazzOfT.newInstance();
        } catch (Exception e) {
            log.error("getConvertData ERR",e);
            throw new ComException(ErrorCode.E999);
        }

        Class<?> c = t.getClass();

        Field[] fields = c.getDeclaredFields();

        int i = 0;

        try {
            for(Field f : fields) {
                f.setAccessible(true);
                f.set(t,rowData.get(i));
                i++;
            }
        } catch (Exception e) {
            log.error("getConvertData ERR",e);
            throw new ComException(ErrorCode.E999);
        }

        return t;
    }
}
