package shop.metacoding.bank.domain.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // TODO 추후에 수정 필요!
    Optional<Account> findByNumber(Long number);

    // JPA Query Method ( SELECT * FROM Account a WHERE a.User.userId = :id )
    List<Account> findByUser_id(Long id);
}
