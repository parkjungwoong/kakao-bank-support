package com.kakaopay.banksupport.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.banksupport.common.constant.ResCode;
import com.kakaopay.banksupport.config.SecurityConfig;
import com.kakaopay.banksupport.config.security.Sha256PasswordEncoder;
import com.kakaopay.banksupport.config.security.jwt.JwtFactory;
import com.kakaopay.banksupport.dto.req.SignInDTO;
import com.kakaopay.banksupport.dto.req.SignupDTO;
import com.kakaopay.banksupport.dto.req.TokenRefreshDTO;
import com.kakaopay.banksupport.dto.res.SignInResDTO;
import com.kakaopay.banksupport.helper.db.TestMapper;
import com.kakaopay.banksupport.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class 추가제약사항TEST {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private TestMapper testMapper;
    @Autowired
    private JwtFactory jwtFactory;
    @Autowired
    private Sha256PasswordEncoder passwordEncoder;

    private static final String 회원가입_URL = "/user/signup";
    private static final String 로그인_URL = "/user/signin";
    private static final String 리프레시토큰_URL = "/api/refreshToken";
    private static String refreshToken;

    @Before
    public void createMockObj() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void A_회원가입() throws Exception {
        /*--------------- given ---------------*/
        //회원가입_URL
        final String id = "pjw";
        final String pw = "pw";

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
    public void B_로그인시_토큰_반환() throws Exception {
        /*--------------- given ---------------*/
        //A->B 단계로 테스트
        final String id = "pjw";
        final String pw = "pw";

        SignInDTO dto = new SignInDTO();
        dto.setId(id);
        dto.setPw(pw);

        /*--------------- when ---------------*/
        MvcResult mvcResult = mockMvc.perform(post(로그인_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(mapper.writeValueAsBytes(dto))
        ).andDo(print())

        /*--------------- then ---------------*/
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.resCode", equalTo(ResCode.S000.name())))
        .andExpect(jsonPath("$.resMsg", equalTo(ResCode.S000.getMessage())))
        .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        SignInResDTO resDTO = mapper.readValue(content, SignInResDTO.class);

        assertThat(resDTO.getAccessToken(), notNullValue());
        assertThat(resDTO.getRefreshToken(), notNullValue());

        refreshToken = resDTO.getRefreshToken();
    }

    @Test
    public void C_리프레시_토큰_요청() throws Exception {
        /*--------------- given ---------------*/
        //!!!A->B-C 단계로 테스트!!!

        final String id = "pjw";

        TokenRefreshDTO dto = new TokenRefreshDTO();
        dto.setUserId(id);

        /*--------------- when ---------------*/
        MvcResult mvcResult = mockMvc.perform(post(리프레시토큰_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .header(SecurityConfig.AUTH_HEADER_KEY, SecurityConfig.AUTH_HEADER_STR+" "+refreshToken)
                .content(mapper.writeValueAsBytes(dto))
        ).andDo(print())

        /*--------------- then ---------------*/
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.resCode", equalTo(ResCode.S000.name())))
        .andExpect(jsonPath("$.resMsg", equalTo(ResCode.S000.getMessage())))
        .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        SignInResDTO resDTO = mapper.readValue(content, SignInResDTO.class);

        assertThat(resDTO.getAccessToken(), notNullValue());
        assertThat(resDTO.getRefreshToken(), notNullValue());
    }

    @Test
    public void D_리프레시_토큰_요청_토큰없으면401() throws Exception {
        /*--------------- given ---------------*/
        //!!!A->B-C 단계로 테스트!!!
        final String id = "pjw";

        TokenRefreshDTO dto = new TokenRefreshDTO();
        dto.setUserId(id);

        /*--------------- when ---------------*/
        MvcResult mvcResult = mockMvc.perform(post(리프레시토큰_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(mapper.writeValueAsBytes(dto))
        ).andDo(print())

        /*--------------- then ---------------*/
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.resCode", equalTo(ResCode.E009.name())))
        .andExpect(jsonPath("$.resMsg", equalTo(ResCode.E009.getMessage())))
        .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        SignInResDTO resDTO = mapper.readValue(content, SignInResDTO.class);
    }
}
