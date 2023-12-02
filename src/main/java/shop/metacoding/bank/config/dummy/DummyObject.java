package shop.metacoding.bank.config.dummy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import shop.metacoding.bank.domain.account.Account;
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

    protected Account newAccount(Long number, User user){
        return Account.builder()
                .number(number)
                .password(3427L)
                .balance(1000L)
                .user(user)
                .build();
    }

    protected Account newMockAccount(Long id, Long number, Long balance,  User user){
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
}
