package shop.metacoding.bank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.metacoding.bank.domain.account.Account;
import shop.metacoding.bank.domain.account.AccountRepository;
import shop.metacoding.bank.domain.transaction.Transaction;
import shop.metacoding.bank.domain.transaction.TransactionEnum;
import shop.metacoding.bank.domain.transaction.TransactionRepository;
import shop.metacoding.bank.domain.user.User;
import shop.metacoding.bank.domain.user.UserRepository;
import shop.metacoding.bank.handler.ex.CustomApiException;

import java.util.List;
import java.util.Optional;

import static shop.metacoding.bank.dto.account.AccountReqDto.*;
import static shop.metacoding.bank.dto.account.AccountRespDto.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public AccountSaveRespDto 계좌등록(AccountSaveReqDto dto, Long userId){
        // User 검증
        User userPS = userRepository.findById(userId).orElseThrow(
                () -> new CustomApiException("유저를 찾을 수  없습니다.")
        );
        // 중복계좌 검증
        Optional<Account> accountOP = accountRepository.findByNumber(dto.getNumber());
        if(accountOP.isPresent()){
            throw new CustomApiException("계좌 중복!");
        }
        // 계좌 등록
        Account accountPS = accountRepository.save(dto.toEntity(userPS));
        // DTO 응답
        return new AccountSaveRespDto(accountPS);
    }

    public AccountListRespDto 계좌목록보기_유저별(Long userId){
        User userPS = userRepository.findById(userId).orElseThrow(
                () -> new CustomApiException("유저를 찾을 수 없습니다.")
        );

        List<Account> accountListPS = accountRepository.findByUser_id(userId);

        return new AccountListRespDto(userPS,accountListPS);
    }

    @Transactional
    public void 계좌삭제(Long accountNumber, Long userId){
        // 1. 계좌확인
        Account accountPS = accountRepository.findByNumber(accountNumber).orElseThrow(
                () -> new CustomApiException("계좌를 찾을 수 없습니다.")
        );
        // 2. 계좌 소유자 확인
        accountPS.checkOwner(userId);
        // 3. 계좌 삭제(응답할 데이터가 필요없다.)
        accountRepository.deleteById(accountPS.getId());
    }

    @Transactional
    public AccountDepositRespDto 계좌입금(AccountDepositReqDto accountDepositReqDto){
        // 0원인지 체크
        if(accountDepositReqDto.getAmount() <= 0L){
            throw new CustomApiException("0원 이하의 금액을 입금할 수 없습니다.");
        }

        // 입금계좌 확인
        Account depositAccountPS = accountRepository.findByNumber(accountDepositReqDto.getNumber()).orElseThrow(
                () -> new CustomApiException("계좌를 찾을 수 없습니다.")
        );

        // 입금
        depositAccountPS.deposit(accountDepositReqDto.getAmount());

        // 거래내역 남기기
        Transaction transaction = Transaction.builder()
                .withdrawAccount(null)
                .depositAccount(depositAccountPS)
                .withdrawAccountBalance(null)
                .depositAccountBalance(depositAccountPS.getBalance())
                .amount(accountDepositReqDto.getAmount())
                .gubun(TransactionEnum.DEPOSIT)
                .sender("ATM")
                .receiver(accountDepositReqDto.getNumber() + "")
                .tel(accountDepositReqDto.getTel())
                .build();
        Transaction transactionPS = transactionRepository.save(transaction);

        return new AccountDepositRespDto(depositAccountPS, transactionPS);
    }

    @Transactional
    public AccountWithdrawRespDto 계좌출금(AccountWithdrawReqDto accountWithdrawReqDto, Long userId){
        // 0원인지 체크
        if(accountWithdrawReqDto.getAmount() <= 0L){
            throw new CustomApiException("0원 이하의 금액을 입금할 수 없습니다.");
        }

        // 출금계좌 확인
        Account withdrawAccountPS = accountRepository.findByNumber(accountWithdrawReqDto.getNumber()).orElseThrow(
                () -> new CustomApiException("계좌를 찾을 수 없습니다.")
        );

        // 출금 소유자 확인
        withdrawAccountPS.checkOwner(userId);

        // 출금계좌 비밀번호 확인
        withdrawAccountPS.checkSamePassword(accountWithdrawReqDto.getPassword());

        // 출금하기(동시에 출금금액과 잔액을 확인)
        withdrawAccountPS.withdraw(accountWithdrawReqDto.getAmount());

        // 거래내역 남기기(내 계좌 -> ATM 으로 출금
        Transaction transaction = Transaction.builder()
                .withdrawAccount(withdrawAccountPS)
                .depositAccount(null)
                .withdrawAccountBalance(withdrawAccountPS.getBalance())
                .depositAccountBalance(null)
                .amount(accountWithdrawReqDto.getAmount())
                .gubun(TransactionEnum.WITHDRAW)
                .sender(withdrawAccountPS.getNumber()+"")
                .receiver("ATM")
                .tel(null)
                .build();

        Transaction transactionPS = transactionRepository.save(transaction);

        return new AccountWithdrawRespDto(withdrawAccountPS,transactionPS);
    }

    @Transactional
    public AccountTransferRespDto 계좌이체(AccountTransferReqDto accountTransferReqDto, Long userId){
        // 출금계좌와 입금계좌가 동일하면 안됨
        if(accountTransferReqDto.getWithdrawNumber().equals(accountTransferReqDto.getDepositNumber())){
            throw new CustomApiException("입금계좌와 출금계좌가 동일합니다.");
        }

        // 0원인지 체크
        if(accountTransferReqDto.getAmount() <= 0L){
            throw new CustomApiException("0원 이하의 금액을 입금할 수 없습니다.");
        }

        // 출금계좌 확인
        Account withdrawAccountPS = accountRepository.findByNumber(accountTransferReqDto.getWithdrawNumber()).orElseThrow(
                () -> new CustomApiException("출급계좌를 찾을 수 없습니다.")
        );

        // 입금계좌 확인
        Account depositAccountPS = accountRepository.findByNumber(accountTransferReqDto.getDepositNumber()).orElseThrow(
                () -> new CustomApiException("입금계좌를 찾을 수 없습니다.")
        );

        // 출금 소유자 확인
        withdrawAccountPS.checkOwner(userId);

        // 출금계좌 비밀번호 확인
        withdrawAccountPS.checkSamePassword(accountTransferReqDto.getWithdrawPassword());

        // 이체하기(출금금액과 잔액을 확인 -> 출금 -> 이체)
        withdrawAccountPS.withdraw(accountTransferReqDto.getAmount());
        depositAccountPS.deposit(accountTransferReqDto.getAmount());

        // 거래내역 남기기(내 계좌 -> ATM 으로 출금
        Transaction transaction = Transaction.builder()
                .withdrawAccount(withdrawAccountPS)
                .depositAccount(depositAccountPS)
                .withdrawAccountBalance(withdrawAccountPS.getBalance())
                .depositAccountBalance(depositAccountPS.getBalance())
                .amount(accountTransferReqDto.getAmount())
                .gubun(TransactionEnum.TRANSFER)
                .sender(accountTransferReqDto.getWithdrawNumber()+"")
                .receiver(accountTransferReqDto.getDepositNumber()+"")
                .build();

        Transaction transactionPS = transactionRepository.save(transaction);

        return new AccountTransferRespDto(withdrawAccountPS,transactionPS);
    }
}
