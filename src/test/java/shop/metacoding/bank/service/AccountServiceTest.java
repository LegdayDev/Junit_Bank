package shop.metacoding.bank.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.metacoding.bank.config.dummy.DummyObject;
import shop.metacoding.bank.domain.account.Account;
import shop.metacoding.bank.domain.account.AccountRepository;
import shop.metacoding.bank.domain.transaction.Transaction;
import shop.metacoding.bank.domain.transaction.TransactionEnum;
import shop.metacoding.bank.domain.transaction.TransactionRepository;
import shop.metacoding.bank.domain.user.User;
import shop.metacoding.bank.domain.user.UserRepository;
import shop.metacoding.bank.handler.ex.CustomApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static shop.metacoding.bank.dto.account.AccountReqDto.*;
import static shop.metacoding.bank.dto.account.AccountRespDto.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest extends DummyObject {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionRepository transactionRepository;

    @Spy // 진짜 객체를 InjectMock 에 주입
    private ObjectMapper om;

    @Test
    public void 계좌등록_test() throws Exception {
        //given
        Long userId = 1L;

        AccountSaveReqDto dto = new AccountSaveReqDto();
        dto.setNumber(4016L);
        dto.setPassword(3427L);

        // stub 1
        User cristiano = newMockUser(userId, "cristiano", "ronaldo");
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(cristiano));

        // stub 2
        Mockito.when(accountRepository.findByNumber(Mockito.any())).thenReturn(Optional.empty());

        // stub 3
        Account cristianoAccount = newMockAccount(userId, 4016L, 1000L, cristiano);
        Mockito.when(accountRepository.save(Mockito.any())).thenReturn(cristianoAccount);

        //when
        AccountSaveRespDto accountSaveRespDto = accountService.계좌등록(dto, cristiano.getId());
        String responseBody = om.writeValueAsString(accountSaveRespDto);

        //then
        assertThat(accountSaveRespDto.getNumber()).isEqualTo(4016L);
    }

    // TODO - Junit 중급강의 종료후 코드 비교해보기
    @Test
    public void 계좌목록보기_유저별_test() throws Exception {
        //given
        Long userId = 1L;

        //stub 1
        User cristiano = newMockUser(userId, "cristiano", "ronaldo");
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(cristiano));

        //stub 2
        Account cristianoAccount1 = newMockAccount(userId, 4016L, 1000L, cristiano);
        Account cristianoAccount2 = newMockAccount(userId, 3427L, 1000L, cristiano);
        List<Account> accounts = new ArrayList<>();
        accounts.add(cristianoAccount1);
        accounts.add(cristianoAccount2);
        Mockito.when(accountRepository.findByUser_id(userId)).thenReturn(accounts);

        //when
        AccountListRespDto accountListRespDto = accountService.계좌목록보기_유저별(userId);

        //then
        assertThat(accountListRespDto.getAccounts().size()).isEqualTo(2);
    }

    @Test
    public void 계좌삭제_test() throws Exception {
        //given
        Long number = 1111L;
        Long userId = 2L;

        //stub
        User cristiano = newMockUser(1L, "cristiano", "ronaldo");
        Account cristianoAccount = newMockAccount(1L, 1111L, 1000L, cristiano);
        Mockito.when(accountRepository.findByNumber(Mockito.any())).thenReturn(Optional.of(cristianoAccount));

        //when
        assertThrows(CustomApiException.class, () -> accountService.계좌삭제(number, userId));
    }

    @Test
    public void 계좌입금_test() throws Exception {
        //given
        AccountDepositReqDto accountDepositReqDto = new AccountDepositReqDto();
        accountDepositReqDto.setNumber(1111L);
        accountDepositReqDto.setAmount(100L);
        accountDepositReqDto.setGubun("DEPOSIT");
        accountDepositReqDto.setTel("01040163427");

        // stub 1
        User cristiano = newMockUser(1L, "cristiano", "ronaldo");
        Account account = newMockAccount(1L, 1111L, 1000L, cristiano);
        Mockito.when(accountRepository.findByNumber(Mockito.any())).thenReturn(Optional.of(account));

        // stub 2
        Account account2 = newMockAccount(1L, 1111L, 1000L, cristiano);
        Transaction transaction = newMockDepositTransaction(1L, account2);
        Mockito.when(transactionRepository.save(Mockito.any())).thenReturn(transaction);


        //when
        AccountDepositRespDto accountDepositRespDto = accountService.계좌입금(accountDepositReqDto);
        String responseBody = om.writeValueAsString(accountDepositRespDto);
        System.out.println("responseBody = " + responseBody);

        //then
        assertThat(accountDepositRespDto.getTransaction().getDepositAccountBalance()).isEqualTo(1100L);
        assertThat(account.getBalance()).isEqualTo(1100L);
    }

    @Test
    @DisplayName("계좌입금() : 0원 이하 금액 들어오는 테스트")
    public void 계좌입금_test2() throws Exception {
        //given
        AccountDepositReqDto accountDepositReqDto = new AccountDepositReqDto();
        accountDepositReqDto.setNumber(1111L);
        accountDepositReqDto.setAmount(0L);
        accountDepositReqDto.setGubun("DEPOSIT");
        accountDepositReqDto.setTel("01040163427");

        //when
        assertThrows(CustomApiException.class, () -> accountService.계좌입금(accountDepositReqDto));

        //then
    }

    @Test
    @DisplayName("Mock 을 이용하지 않고 순수한 로직 테스트")
    public void 계좌출금_test() throws Exception {
        //given
        Long failAmount = 0L;
        Long failUserId = 2L;
        Long failPassword = 4016L;
        Long withdrawAmount = 2000L;

        User cristiano = newMockUser(1L, "cristiano", "ronaldo");
        Account cristianoAccount = newMockAccount(1L, 1111L, 1000L, cristiano);

        //when


        //then
        assertThrows(CustomApiException.class, () -> {
            if (failAmount <= 0L) {
                throw new CustomApiException("0원 이하의 금액을 출금할 수 없습니다.");
            }
        });
        assertThrows(CustomApiException.class, () -> cristianoAccount.checkOwner(failUserId));
        assertThrows(CustomApiException.class, () -> cristianoAccount.checkSamePassword(failPassword));
        assertThrows(CustomApiException.class, () -> cristianoAccount.withdraw(withdrawAmount));

    }

    @Test
    @DisplayName("Mockito 가 아닌 로직 테스트")
    public void 계좌이체_test() throws Exception {
        //given
        AccountTransferReqDto reqDto = new AccountTransferReqDto();
        reqDto.setWithdrawNumber(1111L);
        reqDto.setDepositNumber(2222L);
        reqDto.setWithdrawPassword(3427L);
        reqDto.setAmount(100L);
        reqDto.setGubun("TRANSFER");

        User cristiano = newMockUser(1L,"cristiano","ronaldo");
        User messi = newMockUser(2L,"messi","lionel");
        Account withdrawAccount = newMockAccount(1L,1111L,1000L,cristiano);
        Account depositAccount = newMockAccount(2L,2222L,1000L,messi);

        //when
        // 출금계좌와 입금계좌가 동일한지 체크
        if(reqDto.getWithdrawNumber().equals(reqDto.getDepositNumber())){
            throw new CustomApiException("입금계좌와 출금계좌가 동일합니다");
        }
        // 이체금액이 0원인지 체크
        if(reqDto.getAmount() <= 0L){
            throw new CustomApiException("0원 이하의 금액을 이체할 수 없습니다.");
        }
        // 출금 소유자 확인
        withdrawAccount.checkOwner(1L);
        // 출금계좌 비밀번호 확인
        withdrawAccount.checkSamePassword(reqDto.getWithdrawPassword());
        // 이체하기
        withdrawAccount.withdraw(reqDto.getAmount());
        depositAccount.deposit(reqDto.getAmount());

        //then
        assertThat(withdrawAccount.getBalance()).isEqualTo(900L);
    }
}