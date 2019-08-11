package com.kakaopay.banksupport.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
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
