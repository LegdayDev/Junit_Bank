package shop.metacoding.bank.dto.account;

import lombok.Data;
import shop.metacoding.bank.domain.account.Account;

public class AccountReqDto {
    @Data
    public static class AccountSaveRespDto{
        private Long id;
        private Long number;
        private Long balance;

        public AccountSaveRespDto(Account account) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
        }
    }
}
