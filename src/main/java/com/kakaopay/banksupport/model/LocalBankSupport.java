package com.kakaopay.banksupport.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Alias("LocalBankSupport")
public class LocalBankSupport {
    private String lbsId;
    private String regionId;
    private String regionNm;
    private String target;
    private String usage;
    private long supportLimit;
    private double rateMin;
    private double rateAvg;
    private double rateMax;
    private String institute;
    private String mgmt;
    private String reception;
    private String instId;
    private LocalDateTime instDtm;
    private String updtId;
}
