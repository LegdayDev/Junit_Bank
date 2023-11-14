package shop.metacoding.bank.dto.user;

import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import shop.metacoding.bank.domain.user.User;
import shop.metacoding.bank.domain.user.UserEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserReqDto {
    @Data
    public static class JoinReqDto {

        // 영문, 숫자되고 길이는 최소 2~20자 이내
        @Pattern(regexp = "^[a-zA-Z0-9]{2,20}$", message = "영문 or 숫자 2~20자 이내로 작성해주세요")
        @NotBlank // NULL or 공백
        private String username;

        // 길이 4~20
        @NotBlank
        @Size(min = 4,max = 20) //Size 는 String 만
        private String password;

        // 이메일 형식
        @Pattern(regexp = "^[a-zA-Z0-9]{2,10}@[a-zA-Z0-9]{2,6}\\.[a-zA-Z]{2,3}$", message = "이메일 형식으로 작성해주세요")
        @NotBlank
        private String email;

        // 영어, 한글, 1~20
        @NotBlank
        @Pattern(regexp = "^^[a-zA-Z가-힣]{1,20}$", message = "영문 or 한글 1~20자 이내로 작성해주세요")
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
