package shop.metacoding.bank.domain.transaction;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import shop.metacoding.bank.domain.account.Account;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "transaction_tb")
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private Account withdrawAccount;

    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private Account depositAccount;

    private Long amount;

    private Long withdrawAccountBalance; // 1111 계좌(1000) -> 2222계좌로 500원 이체 : 1111계좌(500)
    private Long depositAccountBalance;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionEnum gubun; // WITHDRAW, DEPOSIT, TRANSFER, ALL

    // 계좌가 사라져도 로그는 남아야 한다.
    private String sender;
    private String receiver;
    private String tel;

    @CreatedDate //INSERT 시 자동생성
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate //INSERT or, UPDATE
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Transaction(Long id, Account withdrawAccount, Account depositAccount, Long amount, Long withdrawAccountBalance,
                       Long depositAccountBalance, TransactionEnum gubun,
                       String sender, String receiver, String tel, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.withdrawAccount = withdrawAccount;
        this.depositAccount = depositAccount;
        this.amount = amount;
        this.withdrawAccountBalance = withdrawAccountBalance;
        this.depositAccountBalance = depositAccountBalance;
        this.gubun = gubun;
        this.sender = sender;
        this.receiver = receiver;
        this.tel = tel;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
