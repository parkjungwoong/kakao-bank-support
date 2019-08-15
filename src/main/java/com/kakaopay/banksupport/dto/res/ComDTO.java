package com.kakaopay.banksupport.dto.res;

import com.kakaopay.banksupport.common.constant.ResCode;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ComDTO {
    private String resCode;
    private String resMsg;

    public ComDTO(ResCode resCode) {
        this.resCode = resCode.name();
        this.resMsg = resCode.getMessage();
    }

    public void setResCode(ResCode resCode) {
        this.resCode = resCode.name();
        this.resMsg = resCode.getMessage();
    }
}
