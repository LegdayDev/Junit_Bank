package shop.metacoding.bank.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import shop.metacoding.bank.config.dummy.DummyObject;
import shop.metacoding.bank.domain.user.UserRepository;
import shop.metacoding.bank.dto.user.UserReqDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static shop.metacoding.bank.dto.user.UserReqDto.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserControllerTest extends DummyObject {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper om;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp(){
        dataSetting();
    }


    @Test
    @DisplayName("회원가입 성공 테스트")
    public void join_success_test() throws Exception {
        //given
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setUsername("love");
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("love@nate.com");
        joinReqDto.setFullName("러브");

        String requestBody = om.writeValueAsString(joinReqDto);

        //when
        ResultActions resultActions = mvc.perform(post("/api/join").
                content(requestBody).
                contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();

        //then
        resultActions.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("회원가입 실패 테스트")
    public void join_fail_test() throws Exception {
        //given
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setUsername("ssar");
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("ssar@nate.com");
        joinReqDto.setFullName("쌀");

        String requestBody = om.writeValueAsString(joinReqDto);

        //when
        ResultActions resultActions = mvc.perform(post("/api/join").
                content(requestBody).
                contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();

        //then
        resultActions.andExpect(status().isBadRequest());
    }


    private void dataSetting() {
        userRepository.save(newUser("ssar","쌀"));
    }
}
