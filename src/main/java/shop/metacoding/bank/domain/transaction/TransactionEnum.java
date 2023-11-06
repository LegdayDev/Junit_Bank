package shop.metacoding.bank.domain.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionEnum {
    WITHDRAW("출급"), DEPOSIT("입금"), TRANSFER("이체"), ALL("입출금내역");

    private String value;
}
