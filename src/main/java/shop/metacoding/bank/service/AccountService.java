package shop.metacoding.bank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.metacoding.bank.domain.account.Account;
import shop.metacoding.bank.domain.account.AccountRepository;
import shop.metacoding.bank.domain.user.User;
import shop.metacoding.bank.domain.user.UserRepository;
import shop.metacoding.bank.handler.ex.CustomApiException;
import java.util.Optional;

import static shop.metacoding.bank.dto.account.AccountReqDto.*;
import static shop.metacoding.bank.dto.account.AccountRespDto.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

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
}
