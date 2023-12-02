package shop.metacoding.bank.domain.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // TODO 추후에 수정 필요!
    Optional<Account> findByNumber(Long number);
}
