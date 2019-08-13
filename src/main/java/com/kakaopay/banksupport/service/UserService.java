package com.kakaopay.banksupport.service;

import com.kakaopay.banksupport.dto.SignInDTO;
import com.kakaopay.banksupport.model.UserInfo;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public UserInfo selectUserInfo(SignInDTO signInDTO) {

        //todo:
        signInDTO.getId();
        signInDTO.getPw();

        return UserInfo.builder()
                .userId("")
                .userPw("")
                .build();
    }
}
