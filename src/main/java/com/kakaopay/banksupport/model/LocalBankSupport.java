package com.kakaopay.banksupport.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("LocalBankSupport")
public class LocalBankSupport {
    private String lbsId;
    private String regionId;
    private String target;
    private String usage;
    private String supportLimit;
    private String rate;
    private String institute;
    private String mgmt;
    private String reception;
}
