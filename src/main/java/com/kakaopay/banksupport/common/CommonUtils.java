package com.kakaopay.banksupport.common;

import com.kakaopay.banksupport.common.constant.ErrorCode;
import com.kakaopay.banksupport.common.exception.ComException;
import com.kakaopay.banksupport.dto.CsvDTO;
import lombok.extern.slf4j.Slf4j;

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
}
