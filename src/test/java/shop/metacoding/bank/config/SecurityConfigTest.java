package shop.metacoding.bank.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.*;

@AutoConfigureMockMvc // Mock(가짜) 환경에 MockMvc 가 등록
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) // 가짜 환경 MOCK 에서 테스트
class SecurityConfigTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("인증 테스트")
    public void authenticationTest() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.get("/api/s/hello"));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        int httpStatusCode = resultActions.andReturn().getResponse().getStatus();
        System.out.println("responseBody = " + responseBody);
        System.out.println("httpStatusCode = " + httpStatusCode);
        // then
        assertThat(httpStatusCode).isEqualTo(401);
    }

    @Test
    @DisplayName("권한 테스트")
    public void authorizationTest() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.get("/api/admin/hello"));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        int httpStatusCode = resultActions.andReturn().getResponse().getStatus();
        System.out.println("responseBody = " + responseBody);
        System.out.println("httpStatusCode = " + httpStatusCode);
        // then
        assertThat(httpStatusCode).isEqualTo(401);
    }
}