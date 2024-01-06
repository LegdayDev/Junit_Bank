package shop.metacoding.bank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.metacoding.bank.domain.account.Account;
import shop.metacoding.bank.domain.account.AccountRepository;
import shop.metacoding.bank.domain.transaction.Transaction;
import shop.metacoding.bank.domain.transaction.TransactionRepository;
import shop.metacoding.bank.handler.ex.CustomApiException;

import java.util.List;

import static shop.metacoding.bank.dto.transaction.TransactionRespDto.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionListRespDto 입출금목록보기(Long userId, Long accountNumber, String gubun, int page) {
        Account accountPS = accountRepository.findByNumber(accountNumber).orElseThrow(
                () -> new CustomApiException("계좌가 유효하지 않습니다!")
        ); // 유효한 계좌인치 확인

        accountPS.checkOwner(userId); // 올바른 사용자가 요청했는지 확인

        List<Transaction> transactionListPS =
                transactionRepository.findTransactionList(accountPS.getId(), gubun, page);

        return new TransactionListRespDto(accountPS, transactionListPS);
    }
}
