package com.kakaopay.banksupport.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("UsageOpt")
public class UsageOpt {
    private String usageOptId;
    private String usageCodeId;
    private String lbsId;
    private String useYn;
}
