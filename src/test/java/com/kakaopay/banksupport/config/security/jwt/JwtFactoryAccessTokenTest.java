package com.kakaopay.banksupport.config.security.jwt;

import com.kakaopay.banksupport.common.constant.ErrorCode;
import com.kakaopay.banksupport.common.exception.ComException;
import com.kakaopay.banksupport.config.ComSettings;
import com.kakaopay.banksupport.model.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class JwtFactoryAccessTokenTest {
    
    @Autowired
    private JwtFactory jwtFactory;

    @Autowired
    private ComSettings comSettings;

    private final String TEST_USER_NO = "201908131234";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void 엑세스토큰생성_성공() {
        /*--------------- given ---------------*/
        UserInfo userInfo = UserInfo.builder().userNo(TEST_USER_NO).build();

        /*--------------- when ---------------*/
        JwtToken accessToken = jwtFactory.getAccessToken(userInfo);

        /*--------------- then ---------------*/
        assertThat(accessToken.getUserInfo(), notNullValue());
        assertThat(accessToken.getUserInfo().getUserNo(), is(TEST_USER_NO));
        assertThat(accessToken.getToken(), notNullValue());
    }

    @Test
    public void 회원번호없이는_생성안됨() {
        expectedException.expect(ComException.class);
        expectedException.expectMessage(ErrorCode.E005.getMessage());

        /*--------------- given ---------------*/
        UserInfo userInfo = UserInfo.builder().userNo("").build();

        /*--------------- when ---------------*/
        jwtFactory.getAccessToken(userInfo);

        /*--------------- then ---------------*/
        //exception
    }

    @Test
    public void 토큰만료일_확인() {
        /*--------------- given ---------------*/
        UserInfo userInfo = UserInfo.builder().userNo(TEST_USER_NO).build();
        comSettings.setJwtAccessExpTime(999);

        /*--------------- when ---------------*/
        JwtToken accessToken = jwtFactory.getAccessToken(userInfo);
        Jws<Claims> claimsJws = JwtParser.parseToken(accessToken.getToken(), comSettings.getJwtSigningKey());

        /*--------------- then ---------------*/
        //todo: 작성하기
    }

    @Test
    public void 토큰_Subject는_회원번호() {
    }
}