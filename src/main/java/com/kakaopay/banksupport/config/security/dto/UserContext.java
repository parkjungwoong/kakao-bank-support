package com.kakaopay.banksupport.config.security.dto;

import com.kakaopay.banksupport.common.constant.ResCode;
import com.kakaopay.banksupport.config.security.exception.ComAuthException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Slf4j
@Getter
public class UserContext {
    private String userNo;
    private List<GrantedAuthority> authorities;

    private UserContext(String userNo, List<GrantedAuthority> authorities) {
        this.userNo = userNo;
        this.authorities = authorities;
    }

    public static UserContext create(String userNo, List<GrantedAuthority> authorities) {
        if(StringUtils.isEmpty(userNo)) {
            log.error("UserContext 생성시 userNo는 필수값");
            throw new ComAuthException(ResCode.E999);
        }

        return new UserContext(userNo, authorities);
    }
}
