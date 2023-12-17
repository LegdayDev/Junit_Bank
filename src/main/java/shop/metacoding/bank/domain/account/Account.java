package shop.metacoding.bank.domain.account;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import shop.metacoding.bank.domain.user.User;
import shop.metacoding.bank.handler.ex.CustomApiException;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "account_tb")
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 4)
    private Long number; // 계좌번호

    @Column(nullable = false, length = 4)
    private Long password; // 계좌비번

    @Column(nullable = false)
    private Long balance; // 잔액(기본값 1000원)

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @CreatedDate //INSERT 시 자동생성
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate //INSERT or, UPDATE
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Account(Long id, Long number, Long password, Long balance,
                   User user, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.number = number;
        this.password = password;
        this.balance = balance;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void checkOwner(Long userId) {
        if (!this.user.getId().equals(userId)) {
            throw new CustomApiException("계좌 소유자가 아닙니다.");
        }
    }

    public void deposit(Long amount) {
        balance += amount;
    }

    public void checkSamePassword(Long password) {
        if(!this.password.equals(password)){
            throw new CustomApiException("계좌 비밀번호 검증에 실패했습니다.");
        }
    }

    public void withdraw(Long amount) {
        if(this.balance < amount){
            throw new CustomApiException("계좌 잔액이 부족합니다.");
        }
        balance-=amount;
    }
}
