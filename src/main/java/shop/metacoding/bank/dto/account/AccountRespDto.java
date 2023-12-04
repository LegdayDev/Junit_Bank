package shop.metacoding.bank.dto.account;

import lombok.Data;
import shop.metacoding.bank.domain.account.Account;
import shop.metacoding.bank.domain.user.User;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Data
    public static class AccountListRespDto{
        private String fullName;
        private List<AccountDto> accounts = new ArrayList<>();

        public AccountListRespDto(User user, List<Account> accounts) {
            this.fullName = user.getFullName();
            this.accounts = accounts.stream()
                    .map(AccountDto::new)
                    .collect(Collectors.toList());
        }

        @Data
        private static class AccountDto {
            private Long id;
            private Long number;
            private Long balance;

            public AccountDto(Account account) {
                this.id=account.getId();
                this.number = account.getNumber();
                this.balance = account.getBalance();
            }
        }
    }
}
