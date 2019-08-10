package com.kakaopay.banksupport.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("UsageCode")
public class UsageCode {
    private String usageCodeId;
    private String name;
    private String value;
    private String useYn;
}
