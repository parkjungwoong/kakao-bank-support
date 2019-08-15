package com.kakaopay.banksupport.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SignInDTO {
    private String id;
    private String pw;
    private String accessToken;
    private String refreshToken;
}
