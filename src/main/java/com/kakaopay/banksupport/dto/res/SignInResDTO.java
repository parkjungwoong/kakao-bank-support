package com.kakaopay.banksupport.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SignInResDTO extends ComDTO {
    private String accessToken;
    private String refreshToken;
}
