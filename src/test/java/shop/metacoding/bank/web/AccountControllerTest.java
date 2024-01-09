package shop.metacoding.bank.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import shop.metacoding.bank.config.dummy.DummyObject;
import shop.metacoding.bank.domain.account.Account;
import shop.metacoding.bank.domain.account.AccountRepository;
import shop.metacoding.bank.domain.transaction.Transaction;
import shop.metacoding.bank.domain.transaction.TransactionRepository;
import shop.metacoding.bank.domain.user.User;
import shop.metacoding.bank.domain.user.UserRepository;
import shop.metacoding.bank.dto.account.AccountReqDto;
import shop.metacoding.bank.handler.ex.CustomApiException;


import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;
import static shop.metacoding.bank.dto.account.AccountReqDto.*;
import static shop.metacoding.bank.dto.account.AccountRespDto.*;

//@Transactional
@Sql("classpath:db/teardown.sql")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AccountControllerTest extends DummyObject {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private ObjectMapper om;

    @BeforeEach
    public void setUp() {
        dataSetting();
        em.clear();
    }

    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void saveAccount_test() throws Exception {
        //given
        AccountSaveReqDto dto = new AccountSaveReqDto();
        dto.setNumber(9999L);
        dto.setPassword(1234L);

        String requestBody = om.writeValueAsString(dto);
        System.out.println("requestBody = " + requestBody);

        //when
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post("/api/s/account").content(requestBody).contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    // TODO Junit 중급강의 종료 후 코드 비교하기
    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void findUserAccount_test() throws Exception {
        //given

        //when
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.get("/api/s/account/login-user"));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * <h2>Test 시 영속화컨텍스트를 초기화 해야 하는 이유!!</h2>
     * <li>테스트시에는 insert 한 것들이 영속화컨텍스트에 남아 있다.</li>
     * <li>개발 모드와 동일한 환경에으로 테스트 할려면 영속화 컨텍스트를 비워야 한다.</li>
     *
     * @throws Exception
     */
    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void deleteAccount_test() throws Exception {
        //given
        Long number = 1111L;

        //when
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.delete("/api/s/account/" + number));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void depositAccount_test() throws Exception {
        //given
        AccountDepositReqDto accountDepositReqDto = new AccountDepositReqDto();
        accountDepositReqDto.setNumber(1111L);
        accountDepositReqDto.setAmount(100L);
        accountDepositReqDto.setGubun("DEPOSIT");
        accountDepositReqDto.setTel("01040163427");

        String requestBody = om.writeValueAsString(accountDepositReqDto);
        System.out.println("requestBody = " + requestBody);

        //when
        ResultActions resultActions = mvc.perform
                (MockMvcRequestBuilders.post("/api/account/deposit").content(requestBody).contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void withdrawAccount_test() throws Exception {
        //given
        AccountWithdrawReqDto reqDto = new AccountWithdrawReqDto();
        reqDto.setNumber(1111L);
        reqDto.setPassword(3427L);
        reqDto.setAmount(100L);
        reqDto.setGubun("WITHDRAW");

        String requestBody = om.writeValueAsString(reqDto);
        System.out.println("responseBody = " + requestBody);

        //when
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.
                post("/api/s/account/withdraw").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void transferAccount_test() throws Exception {
        //given
        AccountTransferReqDto reqDto = new AccountTransferReqDto();
        reqDto.setWithdrawNumber(1111L);
        reqDto.setDepositNumber(2222L);
        reqDto.setWithdrawPassword(3427L);
        reqDto.setAmount(100L);
        reqDto.setGubun("TRANSFER");

        String requestBody = om.writeValueAsString(reqDto);
        System.out.println("requestBody = " + requestBody);

        //when
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.
                post("/api/s/account/transfer").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void findDetailAccount_test() throws Exception {
        //given
        Long number = 1111L;
        String page = "0"; // 실제 컨트롤러에서 쿼리파라미터로 들어오는 값들은 전부 String 이기 때문.

        //when
        ResultActions resultActions =
                mvc.perform(MockMvcRequestBuilders.get("/api/s/account/" + number)
                        .param("page", page));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        //then
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.data.transactions[0].balance").value(900L));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.data.transactions[1].balance").value(800L));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.data.transactions[2].balance").value(700L));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.data.transactions[3].balance").value(800L));
    }

    private void dataSetting() {
        User ssar = userRepository.save(newUser("ssar", "쌀"));
        User cos = userRepository.save(newUser("cos", "코스,"));
        User love = userRepository.save(newUser("love", "러브"));
        User admin = userRepository.save(newUser("admin", "관리자"));

        Account ssarAccount1 = accountRepository.save(newAccount(1111L, ssar));
        Account cosAccount = accountRepository.save(newAccount(2222L, cos));
        Account loveAccount = accountRepository.save(newAccount(3333L, love));
        Account ssarAccount2 = accountRepository.save(newAccount(4444L, ssar));

        Transaction withdrawTransaction1 = transactionRepository
                .save(newWithdrawTransaction(ssarAccount1, accountRepository));
        Transaction depositTransaction1 = transactionRepository
                .save(newDepositTransaction(cosAccount, accountRepository));
        Transaction transferTransaction1 = transactionRepository
                .save(newTransferTransaction(ssarAccount1, cosAccount, accountRepository));
        Transaction transferTransaction2 = transactionRepository
                .save(newTransferTransaction(ssarAccount1, loveAccount, accountRepository));
        Transaction transferTransaction3 = transactionRepository
                .save(newTransferTransaction(cosAccount, ssarAccount1, accountRepository));
    }
}