package shop.metacoding.bank.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import shop.metacoding.bank.config.auth.LoginUser;
import shop.metacoding.bank.dto.ResponseDto;
import shop.metacoding.bank.dto.transaction.TransactionRespDto;
import shop.metacoding.bank.service.TransactionService;

import static shop.metacoding.bank.dto.transaction.TransactionRespDto.*;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/s/account/{number}/transaction")
    public ResponseEntity<?> findTransactionList(@PathVariable Long number,
                                                 @RequestParam(value = "gubun", defaultValue = "ALL") String gubun,
                                                 @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                 @AuthenticationPrincipal LoginUser loginUser) {

        TransactionListRespDto transactionListRespDto =
                transactionService.입출금목록보기(loginUser.getUser().getId(), number, gubun, page);

        return ResponseEntity.ok().body(new ResponseDto<>(1, "입출금목록보기 성공", transactionListRespDto));
    }
}
