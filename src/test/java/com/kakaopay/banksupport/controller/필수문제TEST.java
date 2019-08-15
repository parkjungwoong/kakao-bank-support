package com.kakaopay.banksupport.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.banksupport.common.constant.LbsSearchColum;
import com.kakaopay.banksupport.common.constant.ResCode;
import com.kakaopay.banksupport.config.SecurityConfig;
import com.kakaopay.banksupport.config.security.jwt.JwtFactory;
import com.kakaopay.banksupport.dto.req.LbsSearchDTO;
import com.kakaopay.banksupport.dto.req.LbsUpdateDTO;
import com.kakaopay.banksupport.helper.db.TestMapper;
import com.kakaopay.banksupport.model.LocalBankSupport;
import com.kakaopay.banksupport.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.DataInput;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class 필수문제TEST {
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
    public void 지원하는_지자체_전체_목록_검색_API() throws Exception {
        /*--------------- given ---------------*/
        //지자체_목록_검색_URL

        /*--------------- when ---------------*/
        mockMvc.perform(get(지자체_목록_검색_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .header(SecurityConfig.AUTH_HEADER_KEY, SecurityConfig.AUTH_HEADER_STR + " " + accessToken)
        ).andDo(print())

        /*--------------- then ---------------*/
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.resCode", equalTo(ResCode.S000.name())))
        .andExpect(jsonPath("$.resMsg", equalTo(ResCode.S000.getMessage())))
        .andExpect(jsonPath("$.list", notNullValue()))
        .andReturn();
    }

    @Test
    public void 지원하는_지자체명을_입력받아_해당_지자체의_지원정보를_출력() throws Exception {
        /*--------------- given ---------------*/
        final String regionNm = "강릉시";
        JSONObject jsonObject = new JSONObject();

        JSONObject match = new JSONObject();
        match.put("regionNm",regionNm);

        jsonObject.put("match", match);

        /*--------------- when ---------------*/
        MvcResult mvcResult = mockMvc.perform(get("/api/search")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .header(SecurityConfig.AUTH_HEADER_KEY, SecurityConfig.AUTH_HEADER_STR+" "+accessToken)
                .content(jsonObject.toString().getBytes())
        ).andDo(print())

        /*--------------- then ---------------*/
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.resCode", equalTo(ResCode.S000.name())))
        .andExpect(jsonPath("$.resMsg", equalTo(ResCode.S000.getMessage())))
        .andExpect(jsonPath("$.list", notNullValue()))
        .andReturn();
        List<Map> resList = getRes(mvcResult);

        assertThat(resList.size(), is(1));
        assertThat(resList.get(0).get(LbsSearchColum.REGION_NM.getColumNm()), is(regionNm));
    }

    @Test
    public void 지원하는_지자체_정보_수정() throws Exception {
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
        assertThat(supportList.get(0).getTarget(), is(newTarget));
    }

    @Test
    public void 지원한도_컬럼에서_지원금액으로_내림차순_정렬() throws Exception {
        /*--------------- given ---------------*/
        //출력 수
        final int limitCnt = 3;

        //출력 필드명
        JSONArray fields = new JSONArray();
        fields.put("regionNm");

        //정렬 기준
        JSONArray sort = new JSONArray();
        sort.put(new JSONObject().put("supportLimit","desc"));
        sort.put(new JSONObject().put("rate",""));


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fields", fields);
        jsonObject.put("sort", sort);
        jsonObject.put("limit",limitCnt);

        /*--------------- when ---------------*/
        MvcResult mvcResult = mockMvc.perform(get("/api/search")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .header(SecurityConfig.AUTH_HEADER_KEY, SecurityConfig.AUTH_HEADER_STR+" "+accessToken)
                .content(jsonObject.toString().getBytes())
        ).andDo(print())

        /*--------------- then ---------------*/
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.resCode", equalTo(ResCode.S000.name())))
        .andExpect(jsonPath("$.resMsg", equalTo(ResCode.S000.getMessage())))
        .andExpect(jsonPath("$.list", notNullValue()))
        .andReturn()
        ;

        List<Map> resList = getRes(mvcResult);

        List<LocalBankSupport> dbData = testMapper.지원정보_원장_검색_쿼리로(
                "SELECT   T2.REGION_NM\n" +
                        "FROM     LOCAL_BANK_SUPPORT T1, REGION_MAS T2\n" +
                        "WHERE    T1.REGION_ID = T2.REGION_ID\n" +
                        "ORDER BY T1.SUPPORT_LIMIT DESC, T1.RATE_AVG ASC LIMIT "+limitCnt);

        //응답 데이터 확인
        assertThat(resList.size(), is(limitCnt));
        for(int i=0; resList.size()>i; i++) {
            String regionNm = (String)resList.get(i).get(LbsSearchColum.REGION_NM.getColumNm());

            assertThat(regionNm, notNullValue());
            assertThat(regionNm, is(dbData.get(i).getRegionNm()));
        }
    }

    @Test
    public void 이차보전_컬럼에서_보전_비율이_가장_작은_추천_기관명_출력() throws Exception {
        /*--------------- given ---------------*/
        //출력 수
        final int limitCnt = 1;

        //출력 필드명
        JSONArray fields = new JSONArray();
        fields.put("institute");

        //정렬 기준
        JSONArray sort = new JSONArray();
        sort.put(new JSONObject().put("rate",""));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fields",fields);
        jsonObject.put("sort", sort);
        jsonObject.put("limit",limitCnt);

        /*--------------- when ---------------*/
        MvcResult mvcResult = mockMvc.perform(get("/api/search")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .header(SecurityConfig.AUTH_HEADER_KEY, SecurityConfig.AUTH_HEADER_STR+" "+accessToken)
                .content(jsonObject.toString().getBytes())
        ).andDo(print())

        /*--------------- then ---------------*/
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.resCode", equalTo(ResCode.S000.name())))
        .andExpect(jsonPath("$.resMsg", equalTo(ResCode.S000.getMessage())))
        .andExpect(jsonPath("$.list", notNullValue()))
        .andReturn()
        ;

        List<Map> resList = getRes(mvcResult);

        List<LocalBankSupport> dbData = testMapper.지원정보_원장_검색_쿼리로(
                "SELECT   INSTITUTE\n" +
                        "FROM     LOCAL_BANK_SUPPORT T1, REGION_MAS T2\n" +
                        "ORDER BY T1.RATE_AVG LIMIT "+limitCnt);

        //응답 데이터 확인
        assertThat(resList.size(), is(limitCnt));
        for(int i=0; resList.size()>i; i++) {
            String institute = (String)resList.get(i).get(LbsSearchColum.INSTITUTE.getColumNm());

            assertThat(institute, notNullValue());
            assertThat(institute, is(dbData.get(i).getInstitute()));
        }
    }

    private List<Map> getRes(MvcResult mvcResult) throws Exception {
        String content = mvcResult.getResponse().getContentAsString();
        Map map = mapper.readValue(content, Map.class);
        return (List<Map>) map.get("list");
    }
}
