package shop.metacoding.bank.domain.transaction;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.metacoding.bank.config.dummy.DummyObject;
import shop.metacoding.bank.domain.account.Account;
import shop.metacoding.bank.domain.account.AccountRepository;
import shop.metacoding.bank.domain.user.User;
import shop.metacoding.bank.domain.user.UserRepository;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class TransactionRepositoryImplTest extends DummyObject {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp(){
        autoIncrementReset();
        dataSetting();
        em.clear(); // Repository 테스트에서는 필수 -> 쿼리문을 제대로 확인하기 위해
    }

    @Test
    public void dataJpa_test() throws Exception {
        List<Transaction> transactionList = transactionRepository.findAll();

        for (Transaction transaction : transactionList) {
            System.out.println("transaction.getId() = " + transaction.getId());
            System.out.println("transaction.getSender() = " + transaction.getSender());
            System.out.println("transaction.getReceiver() = " + transaction.getReceiver());
            System.out.println("transaction.getGubun() = " + transaction.getGubun());
            System.out.println("======================");
        }
    }

    @Test
    public void findTransactionList_all_test() throws Exception {
        //given
        Long accountId = 1L;

        //when
        List<Transaction> transactionListPS = transactionRepository.findTransactionList(accountId,"ALL",0);
        transactionListPS.forEach((t)->{
            System.out.println("t.getId() = " + t.getId());
            System.out.println("t.getGubun() = " + t.getGubun());
            System.out.println("t.getAmount() = " + t.getAmount());
            System.out.println("t.getSender() = " + t.getSender());
            System.out.println("t.getReceiver() = " + t.getReceiver());
            System.out.println("t.getDepositAccountBalance() = " + t.getDepositAccountBalance());
            System.out.println("t.getWithdrawAccountBalance() = " + t.getWithdrawAccountBalance());
            System.out.println("=============================");
        });

        //then
        assertThat(transactionListPS.get(3).getDepositAccountBalance()).isEqualTo(800L);
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

    private void autoIncrementReset() {
        em.createNativeQuery("ALTER TABLE user_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE account_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE transaction_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }
}
