package com.kakaopay.banksupport.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("LocalBankSupportDtl")
public class LocalBankSupportDtl {
    private String lbsDtlId;
    private String lbsId;
    private String usageOptId;
    private String supportLimitMin;
    private String supportLimitAvg;
    private String supportLimitMax;
    private String rateMin;
    private String rateAvg;
    private String rateMax;
}
