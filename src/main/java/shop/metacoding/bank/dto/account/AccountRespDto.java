package shop.metacoding.bank.dto.account;

import lombok.Data;
import shop.metacoding.bank.domain.account.Account;
import shop.metacoding.bank.domain.user.User;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

public class AccountRespDto {
    @Data
    public static class AccountSaveReqDto{
        @NotNull
        @Digits(integer = 4, fraction = 4) // @Size 는 String 만
        private Long number;
        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long password;

        public Account toEntity(User user){
            return Account.builder().
                    number(number).
                    password(password).
                    balance(1000L).
                    user(user).build();
        }
    }
}
