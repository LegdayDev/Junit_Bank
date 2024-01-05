package shop.metacoding.bank.config.dummy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import shop.metacoding.bank.domain.account.Account;
import shop.metacoding.bank.domain.account.AccountRepository;
import shop.metacoding.bank.domain.transaction.Transaction;
import shop.metacoding.bank.domain.transaction.TransactionEnum;
import shop.metacoding.bank.domain.user.User;
import shop.metacoding.bank.domain.user.UserEnum;

import java.time.LocalDateTime;

public class DummyObject {

    protected User newUser(String username, String fullName) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encPassword = passwordEncoder.encode("1234");
        return User.builder()
                .username(username)
                .password(encPassword)
                .email(username + "@nate.com")
                .fullName(fullName)
                .role(UserEnum.CUSTOMER)
                .build();
    }

    protected User newMockUser(Long id, String username, String fullName) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encPassword = passwordEncoder.encode("1234");
        return User.builder()
                .id(id)
                .username(username)
                .password(encPassword)
                .email(username + "@nate.com")
                .fullName(fullName)
                .role(UserEnum.CUSTOMER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    protected Account newAccount(Long number, User user) {
        return Account.builder()
                .number(number)
                .password(3427L)
                .balance(1000L)
                .user(user)
                .build();
    }

    protected Account newMockAccount(Long id, Long number, Long balance, User user) {
        return Account.builder()
                .id(id)
                .number(number)
                .password(3427L)
                .balance(balance)
                .user(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    protected Transaction newMockDepositTransaction(Long id, Account account) {
        account.deposit(100L); // 계좌가 입금이 되고 거래내역이 만들어져야 하기 때문에 세팅이 필요
        return Transaction.builder()
                .id(id)
                .withdrawAccount(null)
                .withdrawAccountBalance(null)
                .depositAccount(account)
                .depositAccountBalance(account.getBalance())
                .amount(100L) // 고정
                .gubun(TransactionEnum.DEPOSIT)
                .sender("ATM")
                .receiver(account.getNumber() + "")
                .tel("01040163427") // 고정
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

    }

    protected Transaction newDepositTransaction(Account account, AccountRepository accountRepository) {
        account.deposit(100L);
        // Repository 테스트에서는 더티체킹이 됨
        // Controller 테스트에서는 더티체킹이 안됨
        if (accountRepository != null) {
            accountRepository.save(account);
        }
        return Transaction.builder()
                .withdrawAccount(null)
                .depositAccount(account)
                .withdrawAccountBalance(null)
                .depositAccountBalance(account.getBalance())
                .amount(100L)
                .gubun(TransactionEnum.DEPOSIT)
                .sender("ATM")
                .receiver(account.getNumber() + "")
                .tel("01040163427")
                .build();
    }

    protected Transaction newWithdrawTransaction(Account account, AccountRepository accountRepository) {
        account.withdraw(100L);
        // Repository 테스트에서는 더티체킹이 됨
        // Controller 테스트에서는 더티체킹이 안됨
        if (accountRepository != null) {
            accountRepository.save(account);
        }
        return Transaction.builder()
                .withdrawAccount(account)
                .depositAccount(null)
                .withdrawAccountBalance(account.getBalance())
                .depositAccountBalance(null)
                .amount(100L)
                .gubun(TransactionEnum.WITHDRAW)
                .sender(account.getNumber() + "")
                .receiver("ATM")
                .build();
    }

    protected Transaction newTransferTransaction(Account withdrawAccount, Account depositAccount, AccountRepository accountRepository) {
        withdrawAccount.withdraw(100L);
        depositAccount.deposit(100L);
        // Repository 테스트에서는 더티체킹이 됨
        // Controller 테스트에서는 더티체킹이 안됨
        if (accountRepository != null) {
            accountRepository.save(depositAccount);
            accountRepository.save(withdrawAccount);
        }
        return Transaction.builder()
                .withdrawAccount(withdrawAccount)
                .depositAccount(depositAccount)
                .withdrawAccountBalance(withdrawAccount.getBalance())
                .depositAccountBalance(depositAccount.getBalance())
                .amount(100L)
                .gubun(TransactionEnum.TRANSFER)
                .sender(withdrawAccount.getNumber() + "")
                .receiver(depositAccount.getNumber() + "")
                .build();
    }
}
