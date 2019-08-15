package com.kakaopay.banksupport.dto.req;

import lombok.Data;

@Data
public class LbsUpdateDTO {
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
