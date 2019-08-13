package com.kakaopay.banksupport.dto;

import lombok.Builder;
import lombok.Data;

public class LocalBankSupportDTO extends ComDTO {
    private String region;
    private String usage;
    private String limit;
    private String rate;
    private String institute;
    //todo ...
}
