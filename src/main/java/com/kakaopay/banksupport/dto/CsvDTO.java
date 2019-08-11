package com.kakaopay.banksupport.dto;

import lombok.Getter;

import java.util.List;

public class CsvDTO {
    @Getter
    private List<String> row;

    private CsvDTO(List<String> row) {
        this.row = row;
    }

    public static CsvDTO CreateCsvDTO(List<String> row) {
        return new CsvDTO(row);
    }

    public String[] getRowData(int index) {
        return row.get(index).split(",");
    }
}
