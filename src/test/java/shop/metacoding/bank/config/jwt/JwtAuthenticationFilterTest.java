package shop.metacoding.bank.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.*;
import org.springframework.transaction.annotation.Transactional;
import shop.metacoding.bank.config.dummy.DummyObject;
import shop.metacoding.bank.domain.user.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static shop.metacoding.bank.dto.user.UserReqDto.*;

@Sql("classpath:db/teardown.sql")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class JwtAuthenticationFilterTest extends DummyObject {

    @Autowired
    private ObjectMapper om;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() throws Exception{
        userRepository.save(newUser("ssar", "쌀"));
    }

    @Test
    public void successfulAuthentication_test() throws Exception {
        //given
        LoginReqDto loginReqDto = new LoginReqDto();
        loginReqDto.setUsername("ssar");
        loginReqDto.setPassword("1234");

        String requestBody = om.writeValueAsString(loginReqDto);
        System.out.println("requestBody = " + requestBody);

        //when
        ResultActions resultActions = mvc.perform(
                post("/api/login").
                content(requestBody).
                contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        String jwtToken = resultActions.andReturn().getResponse().getHeader(JwtVO.HEADER);

        //then
        resultActions.andExpect(status().isOk());
        assertNotNull(jwtToken);
        assertTrue(jwtToken.startsWith(JwtVO.TOKEN_PREFIX));
        resultActions.andExpect(jsonPath("$.data.username").value("ssar"));

    }

    @Test
    public void unsuccessfulAuthentication_test() throws Exception {
        //given
        LoginReqDto loginReqDto = new LoginReqDto();
        loginReqDto.setUsername("ssar");
        loginReqDto.setPassword("123434");

        String requestBody = om.writeValueAsString(loginReqDto);
        System.out.println("요청 데이터 requestBody = " + requestBody);

        //when
        ResultActions resultActions = mvc.perform(
                post("/api/login").
                        content(requestBody).
                        contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("응답 데이터 responseBody : " + responseBody);

        //then
        resultActions.andExpect(status().isUnauthorized());
    }

}