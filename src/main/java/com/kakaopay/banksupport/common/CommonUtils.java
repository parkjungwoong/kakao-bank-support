package com.kakaopay.banksupport.common;

import com.kakaopay.banksupport.common.constant.ResCode;
import com.kakaopay.banksupport.common.exception.ComException;
import com.kakaopay.banksupport.dto.CsvDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.security.MessageDigest;
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
        log.debug("file name : {}",filePath);
        String line;
        List<List<String>> rowList;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "EUC-KR"))) {
            rowList = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                rowList.add(parsingCsvRow(line));
            }
        } catch (IOException e) {
            log.error("readCsvFile ERR",e);
            throw new ComException(ResCode.E999);
        }

        return CsvDTO.CreateCsvDTO(rowList);
    }

    public static List<String> parsingCsvRow(final String row) {
        List<String> parsRow = new ArrayList<>();

        if(StringUtils.isBlank(row)) return parsRow;

        String[] strings = row.split(",");

        boolean dobuleQ = false;

        StringBuffer sb = null;


        for(String e : strings) {
            if(e.startsWith("\"")) {
                dobuleQ = true;
                sb = new StringBuffer();
                sb.append(e,1,e.length());
                sb.append(",");
                continue;
            }

            if(dobuleQ) {
                if(e.endsWith("\"")) {
                    sb.append(e,0,e.length()-1);
                    parsRow.add(sb.toString());
                    dobuleQ = false;
                } else {
                    sb.append(e);
                }
            } else {
                parsRow.add(e);
            }
        }

        return parsRow;
    }

    /**
     * 문자열 퍼센트를 더블형으로 변환
     * @param percentStr 문자열 퍼센트
     * @return 더블형 값
     * @throws ComException E013 파싱 에러
     */
    public static Double strPercentToDouble(final String percentStr) throws ComException {
        if(percentStr == null) {
            log.error("percentStr is null");
            throw new ComException(ResCode.E013);
        }

        String strings = percentStr.trim().split("%")[0];

        try {
            return Double.parseDouble(strings);
        } catch (NumberFormatException e) {
            log.error("percentStr is not number, str => {}",strings);
            throw new ComException(ResCode.E014);
        }
    }

    public static String camelcaseTounderscore(final String camelcaseStr) {
        String regex = "([a-z])([A-Z])";
        String replacement = "$1_$2";
        return camelcaseStr.replaceAll(regex, replacement).toUpperCase();
    }

    public static String sha256(String msg) throws ComException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(msg.getBytes());

            StringBuilder sb = new StringBuilder();

            for(byte b : md.digest()) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }

            return sb.toString();
        } catch (Exception e) {
            log.error("비밀번호 해시 에러",e);
            throw new ComException(ResCode.E999);
        }
    }



}
