package com.kakaopay.banksupport.config.security.provider;

import com.kakaopay.banksupport.config.security.Sha256PasswordEncoder;
import com.kakaopay.banksupport.dto.req.SignInDTO;
import com.kakaopay.banksupport.model.UserInfo;
import com.kakaopay.banksupport.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class LoginReqProviderTest {

    @Mock
    private Sha256PasswordEncoder passwordEncoder;

    @Mock
    private UserService userService;

    private LoginReqProvider loginReqProvider;

    @Before
    public void createObj() {
        loginReqProvider = new LoginReqProvider(userService, passwordEncoder);
    }

    @Test
    public void userService서비스의_selectUserInfo_메소드에서_리턴된_UserInfo가_반환() {
        final String TEST_NO = "123456789";
        final String TEST_ID = "test";
        final String TEST_PW = "testpw";

        /*--------------- given ---------------*/
        UserInfo userInfo = UserInfo.builder().userId(TEST_ID).userPw(TEST_PW).userNo(TEST_NO).build();
        given(userService.selectUserInfo(SignInDTO.builder().id(TEST_ID).pw(TEST_PW).build())).willReturn(userInfo);
        given(passwordEncoder.matches(TEST_PW,TEST_PW)).willReturn(true);

        /*--------------- when ---------------*/
        Authentication authenticate = loginReqProvider.authenticate(new UsernamePasswordAuthenticationToken(TEST_ID, TEST_PW));

        /*--------------- then ---------------*/
        UserInfo principal = (UserInfo)authenticate.getPrincipal();
        assertThat(principal.getUserId(), is(TEST_ID));
        assertThat(principal.getUserNo(), is(TEST_NO));
    }


}