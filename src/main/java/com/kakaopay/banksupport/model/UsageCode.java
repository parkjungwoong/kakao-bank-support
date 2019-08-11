package com.kakaopay.banksupport.model;

import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

@Builder
@Data
@Alias("UsageCode")
public class UsageCode {
    private String usageCodeId;
    private String val;
    private String useYn;
}
