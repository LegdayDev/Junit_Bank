package shop.metacoding.bank.dto.user;

import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import shop.metacoding.bank.domain.user.User;
import shop.metacoding.bank.domain.user.UserEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class UserReqDto {
    @Data
    public static class JoinReqDto {

        @NotBlank // NULL or 공백
        private String username;
        @NotBlank
        private String password;
        @NotBlank
        private String email;
        @NotBlank
        private String fullName;

        public User toEntity(BCryptPasswordEncoder passwordEncoder){
            return User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .email(email)
                    .fullName(fullName)
                    .role(UserEnum.CUSTOMER)
                    .build();
        }
    }
}
