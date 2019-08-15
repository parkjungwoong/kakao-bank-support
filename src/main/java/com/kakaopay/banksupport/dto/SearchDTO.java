package com.kakaopay.banksupport.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.List;
import java.util.Map;

@Alias("SearchDTO")
@Data
public class SearchDTO {
    private List<String> fields;
    private Map<String, Object> match;
    private List<Map<String, String>> sort;
    private int limit;
}
