package com.kakaopay.banksupport.common;

import com.kakaopay.banksupport.common.constant.ErrorCode;
import com.kakaopay.banksupport.common.constant.WonUnit;
import com.kakaopay.banksupport.common.exception.ComException;
import com.kakaopay.banksupport.dto.CsvDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CommonUtils {

    /**
     * csv 파일을 읽어 데이터를 변환하여 반환한다.
     * 제네릭타입에 따른 값을 반환하며 제네릭 타입 미지정시 String으로 반환
     * @param filePath 파일의 경로
     * @return 변환된 데이터
     * @throws ComException ErrorCode.E999
     */
    public static CsvDTO readCsvFile(final String filePath) throws ComException {
        String line;
        List<String> rowList;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "EUC-KR"))) {
            rowList = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                rowList.add(line);
            }
        } catch (IOException e) {
            log.error("readCsvFile ERR",e);
            throw new ComException(ErrorCode.E999);
        }

        return CsvDTO.CreateCsvDTO(rowList);
    }

    /**
     * 한글 원화 표기를 숫자로 변환
     * @param wonStr 한글 원화
     * @return 숫자
     * @throws ComException 변환 가능한 포멧이 아닐때 ErrorCode.E002
     */
    public static long wonUnitToNumber(final String wonStr) throws ComException {

        for(WonUnit wonUnit : WonUnit.values()) {
            String[] strings = wonStr.trim().split(wonUnit.getKorUnit());
            if(StringUtils.isNumeric(strings[0])) {
                return Long.parseLong(strings[0]+wonUnit.getZeroCnt());
            }
        }

        throw new ComException(ErrorCode.E002);
    }

    public static float strPercentToFloat(final String percentStr) {
        String strings = percentStr.trim().split("%")[0];
        return Float.parseFloat(strings);
    }
}
