package com.kakaopay.banksupport.service;

import com.kakaopay.banksupport.common.constant.ResCode;
import com.kakaopay.banksupport.common.exception.ComException;
import com.kakaopay.banksupport.config.ComSettings;
import com.kakaopay.banksupport.config.security.Sha256PasswordEncoder;
import com.kakaopay.banksupport.config.security.jwt.JwtFactory;
import com.kakaopay.banksupport.dto.req.SignInDTO;
import com.kakaopay.banksupport.dto.req.SignupDTO;
import com.kakaopay.banksupport.dto.req.TokenRefreshDTO;
import com.kakaopay.banksupport.dto.res.ComDTO;
import com.kakaopay.banksupport.dto.res.SignInResDTO;
import com.kakaopay.banksupport.mapper.UserMapper;
import com.kakaopay.banksupport.model.AuthTrd;
import com.kakaopay.banksupport.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Sha256PasswordEncoder passwordEncoder;

    @Autowired
    private ComSettings comSettings;

    @Autowired
    private JwtFactory jwtFactory;

    public UserInfo selectUserInfo(SignInDTO signInDTO) throws ComException {
        //todo: validation
        return userMapper.selectUserById(signInDTO.getId());
    }

    public ComDTO signup(SignupDTO dto) throws ComException {
        //todo: validation
        UserInfo userInfo = userMapper.selectUserById(dto.getId());
        if(userInfo != null) throw new ComException(ResCode.E011);

        userMapper.insertUserInfo(
                UserInfo.builder()
                        .userId(dto.getId())
                        .userPw(passwordEncoder.encode(dto.getPw()))
                        .build()
        );

        return new ComDTO(ResCode.S000);
    }

    public void insertAuthTrd(AuthTrd authTrd) {
        userMapper.insertAuthTrd(authTrd);
    }

    public SignInResDTO tokenRefresh(TokenRefreshDTO dto) {
        //todo: validation
        UserInfo userInfo = userMapper.selectUserById(dto.getUserId());
        if(userInfo == null) throw new ComException(ResCode.E008);

        userMapper.insertAuthTrd(AuthTrd.builder().userNo(userInfo.getUserNo()).build());

        //토큰 발급
        SignInResDTO resDTO = SignInResDTO.builder()
                .accessToken(jwtFactory.getAccessToken(userInfo).getToken())
                .refreshToken(jwtFactory.getRefreshToken(userInfo).getToken())
                .build();

        resDTO.setResCode(ResCode.S000);

        return resDTO;
    }
}
