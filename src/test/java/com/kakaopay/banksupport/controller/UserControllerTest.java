package com.kakaopay.banksupport.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.banksupport.common.constant.ResCode;
import com.kakaopay.banksupport.config.security.Sha256PasswordEncoder;
import com.kakaopay.banksupport.dto.req.SignupDTO;
import com.kakaopay.banksupport.helper.db.TestMapper;
import com.kakaopay.banksupport.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private TestMapper testMapper;

    @Autowired
    private Sha256PasswordEncoder passwordEncoder;

    private static final String 회원가입_URL = "/user/signup";

    @Before
    public void createMockObj() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
        /*--------------- given ---------------*/

        /*--------------- when ---------------*/

        /*--------------- then ---------------*/
    }

    @Test
    public void 회원가입() throws Exception {
        /*--------------- given ---------------*/
        //회원가입_URL
        final String id = "pjw2";
        final String pw = "pw2";

        SignupDTO dto = new SignupDTO();
        dto.setId(id);
        dto.setPw(pw);

        /*--------------- when ---------------*/
        mockMvc.perform(post(회원가입_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(mapper.writeValueAsBytes(dto))
        ).andDo(print())

        /*--------------- then ---------------*/
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.resCode", equalTo(ResCode.S000.name())))
        .andExpect(jsonPath("$.resMsg", equalTo(ResCode.S000.getMessage())))
        ;

        List<UserInfo> user = testMapper.회원정보_검색_쿼리로("SELECT * FROM USER_INFO WHERE USER_ID = '" + id+"'");
        UserInfo dbUser = user.get(0);

        assertThat(dbUser.getUserId(), is(id));
        assertThat(dbUser.getUserPw(), is(passwordEncoder.encode(pw)));
        assertThat(dbUser.getUserNo(), notNullValue());
        assertThat(dbUser.getInstDtm(), notNullValue());
        assertThat(dbUser.getInstId(), notNullValue());
        assertThat(dbUser.getUseYn(), is("Y"));
    }

    @Test
    public void 회원가입_아이디중복에러() throws Exception {
        /*--------------- given ---------------*/
        //회원가입_URL
        SignupDTO dto = new SignupDTO();
        dto.setId("myuoong");
        dto.setPw("pw");

        /*--------------- when ---------------*/
        mockMvc.perform(post(회원가입_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(mapper.writeValueAsBytes(dto))
        ).andDo(print())

        /*--------------- then ---------------*/
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.resCode", equalTo(ResCode.E011.name())))
        .andExpect(jsonPath("$.resMsg", equalTo(ResCode.E011.getMessage())))
        ;
    }
}