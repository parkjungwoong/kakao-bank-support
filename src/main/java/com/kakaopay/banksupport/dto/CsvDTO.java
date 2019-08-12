package com.kakaopay.banksupport.dto;

import lombok.Getter;

import java.util.List;

public class CsvDTO {
    @Getter
    private List<List<String>> row;

    private CsvDTO(List<List<String>> row) {
        this.row = row;
    }

    public static CsvDTO CreateCsvDTO(List<List<String>> row) {
        return new CsvDTO(row);
    }

    public List<String> getRowData(int index) {
        return row.get(index);
    }
}
