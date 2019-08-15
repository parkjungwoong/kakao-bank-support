package com.kakaopay.banksupport.config.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.banksupport.common.constant.ResCode;
import com.kakaopay.banksupport.config.security.exception.ComAuthException;
import com.kakaopay.banksupport.config.security.handler.AuthFailHandler;
import com.kakaopay.banksupport.config.security.handler.LoginSuccessHandler;
import com.kakaopay.banksupport.dto.req.SignInDTO;
import com.kakaopay.banksupport.helper.authProvider.FakeProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class LoginReqFilterTest {

    @Autowired private ObjectMapper objectMapper;
    @Autowired private LoginSuccessHandler loginSuccessHandler;
    @Autowired private AuthFailHandler authFailHandler;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static MockHttpServletRequest request;
    private static MockHttpServletResponse response;
    private static LoginReqFilter loginReqFilter;

    @Before
    public void createMockObj(){
        request = new MockHttpServletRequest();
        request.setMethod(HttpMethod.POST.name());
        request.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        response = new MockHttpServletResponse();
        loginReqFilter = new LoginReqFilter(loginSuccessHandler, authFailHandler, objectMapper);
        loginReqFilter.setAuthenticationManager(new ProviderManager(Arrays.asList(new FakeProvider())));
    }

    @Test
    public void Authentication객체엔_요청자_id값() throws IOException, ServletException {
        final String TEST_ID = "userId";
        final String TEST_PW = "password";
        /*--------------- given ---------------*/
        SignInDTO signInDTO = SignInDTO.builder().id(TEST_ID).pw(TEST_PW).build();
        request.setContent(objectMapper.writeValueAsBytes(signInDTO));

        /*--------------- when ---------------*/
        Authentication authentication = loginReqFilter.attemptAuthentication(request, response);

        /*--------------- then ---------------*/
        assertThat(authentication.getPrincipal(), is(TEST_ID));
    }

    @Test
    public void POST메소드만_허용() throws IOException, ServletException {
        expectedException.expect(ComAuthException.class);
        expectedException.expectMessage(ResCode.E003.getMessage());

        /*--------------- given ---------------*/
        request.setMethod(HttpMethod.GET.name());

        /*--------------- when ---------------*/
        loginReqFilter.attemptAuthentication(request,response);

        /*--------------- then ---------------*/
        //exception
    }

    @Test
    public void 아이디_파라미터값은_필수() throws IOException, ServletException {
        expectedException.expect(ComAuthException.class);
        expectedException.expectMessage(ResCode.E005.getMessage());

        /*--------------- given ---------------*/
        SignInDTO signInDTO = SignInDTO.builder().id("").pw("1234").build();
        request.setContent(objectMapper.writeValueAsBytes(signInDTO));

        /*--------------- when ---------------*/
        loginReqFilter.attemptAuthentication(request,response);

        /*--------------- then ---------------*/
        //exception
    }

    @Test
    public void 비밀번호_파라미터값은_필수() throws IOException, ServletException {
        expectedException.expect(ComAuthException.class);
        expectedException.expectMessage(ResCode.E005.getMessage());

        /*--------------- given ---------------*/
        SignInDTO signInDTO = SignInDTO.builder().id("aaa").pw("").build();
        request.setContent(objectMapper.writeValueAsBytes(signInDTO));

        /*--------------- when ---------------*/
        loginReqFilter.attemptAuthentication(request,response);

        /*--------------- then ---------------*/
        //exception
    }

    @Test
    public void 아이디_비밀번호_둘다_파라미터값은_필수() throws IOException, ServletException {
        expectedException.expect(ComAuthException.class);
        expectedException.expectMessage(ResCode.E005.getMessage());

        /*--------------- given ---------------*/
        SignInDTO signInDTO = SignInDTO.builder().id("").pw("").build();
        request.setContent(objectMapper.writeValueAsBytes(signInDTO));

        /*--------------- when ---------------*/
        loginReqFilter.attemptAuthentication(request,response);

        /*--------------- then ---------------*/
        //exception
    }
}