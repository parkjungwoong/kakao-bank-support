package com.kakaopay.banksupport.model;

import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

@Builder
@Data
@Alias("LocalBankSupportDtl")
public class LocalBankSupportDtl {
    private String lbsDtlId;
    private String lbsId;
    private long supportLimit;
    private float rateMin;
    private float rateAvg;
    private float rateMax;
}
