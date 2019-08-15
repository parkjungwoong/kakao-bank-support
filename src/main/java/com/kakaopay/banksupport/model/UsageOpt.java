package com.kakaopay.banksupport.model;

import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

@Builder
@Data
@Alias("UsageOpt")
public class UsageOpt {
    private String usageCodeId;
    private String lbsId;
    private String useYn;

    private LocalBankSupport localBankSupport;
    private UsageCode usageCode;
}
