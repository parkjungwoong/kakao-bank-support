package com.kakaopay.banksupport.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.banksupport.common.constant.ResCode;
import com.kakaopay.banksupport.config.SecurityConfig;
import com.kakaopay.banksupport.config.security.filter.JwtReqFilter;
import com.kakaopay.banksupport.config.security.jwt.JwtAuthenticationToken;
import com.kakaopay.banksupport.config.security.jwt.JwtFactory;
import com.kakaopay.banksupport.config.security.provider.JwtReqProvider;
import com.kakaopay.banksupport.dto.LocalBankSupportDTO;
import com.kakaopay.banksupport.dto.req.LbsUpdateDTO;
import com.kakaopay.banksupport.helper.authProvider.FakeProvider;
import com.kakaopay.banksupport.helper.db.TestMapper;
import com.kakaopay.banksupport.model.LocalBankSupport;
import com.kakaopay.banksupport.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class BankControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private TestMapper testMapper;

    @Autowired
    private JwtFactory jwtFactory;

    private static final String 지자체_목록_검색_URL = "/api/search";
    private static final String 지원정보_수정_URL = "/api/update";
    private static String accessToken;

    @Before
    public void createMockObj() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();

        accessToken = jwtFactory.getAccessToken(UserInfo.builder().userNo("1234").build()).getToken();
        /*--------------- given ---------------*/

        /*--------------- when ---------------*/

        /*--------------- then ---------------*/
    }

    @Test
    public void 전체리스트요청() throws Exception {
        /*--------------- given ---------------*/
        //지자체_목록_검색_URL

        /*--------------- when ---------------*/
        mockMvc.perform(get(지자체_목록_검색_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .header(SecurityConfig.AUTH_HEADER_KEY, SecurityConfig.AUTH_HEADER_STR+" "+accessToken)
        ).andDo(print())

        /*--------------- then ---------------*/
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.resCode", equalTo(ResCode.S000.name())))
        .andExpect(jsonPath("$.resMsg", equalTo(ResCode.S000.getMessage())))
        .andExpect(jsonPath("$.list", notNullValue()))
        ;
    }

    @Test
    public void 검색옵션으로요청() throws Exception {
        /*--------------- given ---------------*/
        JSONObject jsonObject = new JSONObject();

        JSONArray fields = new JSONArray();
        fields.put("regionId");
        fields.put("usage");
        fields.put("institute");

        JSONObject match = new JSONObject();
        match.put("usage","시설");

        //정렬 기준
        JSONArray sort = new JSONArray();
        sort.put(new JSONObject().put("supportLimit","desc"));
        sort.put(new JSONObject().put("rate",""));

        jsonObject.put("fields", fields);
        jsonObject.put("match", match);
        jsonObject.put("sort", sort);
        jsonObject.put("limit",2);

        /*--------------- when ---------------*/
        mockMvc.perform(get("/api/search")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .header(SecurityConfig.AUTH_HEADER_KEY, SecurityConfig.AUTH_HEADER_STR+" "+accessToken)
                .content(jsonObject.toString().getBytes())
        ).andDo(print())

        /*--------------- then ---------------*/
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.resCode", equalTo(ResCode.S000.name())))
        .andExpect(jsonPath("$.resMsg", equalTo(ResCode.S000.getMessage())))
        .andExpect(jsonPath("$.list", notNullValue()))
        ;
    }

    @Test
    public void 지원정보수정요청() throws Exception {
        /*--------------- given ---------------*/
        final String lbsId = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))+"1";
        final String newTarget = "지원 대상 수정";
        //지원정보_수정_URL
        LbsUpdateDTO dto = new LbsUpdateDTO();
        dto.setLbsId(lbsId);
        dto.setTarget(newTarget);

        /*--------------- when ---------------*/
        mockMvc.perform(post(지원정보_수정_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .header(SecurityConfig.AUTH_HEADER_KEY, SecurityConfig.AUTH_HEADER_STR+" "+accessToken)
                .content(mapper.writeValueAsBytes(dto))
        ).andDo(print())

        /*--------------- then ---------------*/
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.resCode", equalTo(ResCode.S000.name())))
        .andExpect(jsonPath("$.resMsg", equalTo(ResCode.S000.getMessage())))
        ;

        List<LocalBankSupport> supportList = testMapper.지원정보_원장_검색_쿼리로("SELECT * FROM LOCAL_BANK_SUPPORT WHERE LBS_ID = " + lbsId);
        log.info("supportList => {}", supportList.get(0).toString());
        assertThat(supportList.get(0).getTarget(), is(newTarget));
    }

    @Test
    public void 지원정보수정요청_미존재_지원정보() throws Exception {
        /*--------------- given ---------------*/
        final String lbsId = "없는 아이디";
        final String newTarget = "지원 대상 수정";
        //지원정보_수정_URL
        LbsUpdateDTO dto = new LbsUpdateDTO();
        dto.setLbsId(lbsId);
        dto.setTarget(newTarget);

        /*--------------- when ---------------*/
        mockMvc.perform(post(지원정보_수정_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .header(SecurityConfig.AUTH_HEADER_KEY, SecurityConfig.AUTH_HEADER_STR+" "+accessToken)
                .content(mapper.writeValueAsBytes(dto))
        ).andDo(print())

        /*--------------- then ---------------*/
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.resCode", equalTo(ResCode.E012.name())))
        .andExpect(jsonPath("$.resMsg", equalTo(ResCode.E012.getMessage())))
        ;
    }

    @Test
    public void 지원정보수정요청_파라미터값오류() throws Exception {
        /*--------------- given ---------------*/
        final String lbsId = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))+"1";
        final String rate = "rate은 '숫자%' 형식어야함";
        //지원정보_수정_URL
        LbsUpdateDTO dto = new LbsUpdateDTO();
        dto.setLbsId(lbsId);
        dto.setRate(rate);

        /*--------------- when ---------------*/
        mockMvc.perform(post(지원정보_수정_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .header(SecurityConfig.AUTH_HEADER_KEY, SecurityConfig.AUTH_HEADER_STR+" "+accessToken)
                .content(mapper.writeValueAsBytes(dto))
        ).andDo(print())

        /*--------------- then ---------------*/
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.resCode", equalTo(ResCode.E014.name())))
        .andExpect(jsonPath("$.resMsg", equalTo(ResCode.E014.getMessage())))
        ;
    }

    @Test
    public void 지원정보수정요청_파라미터값오류2() throws Exception {
        /*--------------- given ---------------*/
        final String lbsId = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))+"1";
        final String supportLimit = "지원한도는 숫자+한글단위어야함"; //WonUnit 참고
        //지원정보_수정_URL
        LbsUpdateDTO dto = new LbsUpdateDTO();
        dto.setLbsId(lbsId);
        dto.setSupportLimit(supportLimit);

        /*--------------- when ---------------*/
        mockMvc.perform(post(지원정보_수정_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .header(SecurityConfig.AUTH_HEADER_KEY, SecurityConfig.AUTH_HEADER_STR+" "+accessToken)
                .content(mapper.writeValueAsBytes(dto))
        ).andDo(print())

        /*--------------- then ---------------*/
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.resCode", equalTo(ResCode.E002.name())))
        .andExpect(jsonPath("$.resMsg", equalTo(ResCode.E002.getMessage())))
        ;
    }
}