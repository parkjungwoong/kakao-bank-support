package com.kakaopay.banksupport.config.security.jwt;

import com.kakaopay.banksupport.config.ComSettings;
import com.kakaopay.banksupport.model.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class JwtParserTest {

    @Autowired
    private JwtFactory jwtFactory;

    @Autowired
    private ComSettings comSettings;

    private static final String TEST_USER_NO = "1234";

    @Test
    public void string형태의_jwt토큰을_Claims타입으로_파싱() {
        /*--------------- given ---------------*/
        String token = jwtFactory.getAccessToken(UserInfo.builder().userNo(TEST_USER_NO).build()).getToken();

        /*--------------- when ---------------*/
        Jws<Claims> claimsJws = JwtParser.parseToken(token, comSettings.getJwtSigningKey());

        /*--------------- then ---------------*/
        assertThat(claimsJws.getBody().getSubject(),is(TEST_USER_NO));
    }

    //todo: test 작성하기
}