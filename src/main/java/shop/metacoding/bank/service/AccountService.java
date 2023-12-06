package shop.metacoding.bank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.metacoding.bank.domain.account.Account;
import shop.metacoding.bank.domain.account.AccountRepository;
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
}
