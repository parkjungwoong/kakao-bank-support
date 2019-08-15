package com.kakaopay.banksupport.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Alias("LbsSearchDTO")
@Builder
@Data
public class LbsSearchDTO {
    private List<String> fields;
    private String match;
    private String sort;
    private int limit;
}
