package com.kakaopay.banksupport.mapper;

import com.kakaopay.banksupport.model.AuthTrd;
import com.kakaopay.banksupport.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {
    UserInfo selectUserById(String userId);

    void insertUserInfo(UserInfo userInfo);

    void insertAuthTrd(AuthTrd authTrd);

    AuthTrd selectAuthTrd(AuthTrd authTrd);
}
