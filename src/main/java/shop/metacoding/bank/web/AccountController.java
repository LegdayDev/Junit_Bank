package shop.metacoding.bank.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import shop.metacoding.bank.config.auth.LoginUser;
import shop.metacoding.bank.dto.ResponseDto;
import shop.metacoding.bank.dto.account.AccountReqDto;
import shop.metacoding.bank.handler.ex.CustomForbiddenException;
import shop.metacoding.bank.service.AccountService;

import javax.validation.Valid;

import static shop.metacoding.bank.dto.account.AccountReqDto.*;
import static shop.metacoding.bank.dto.account.AccountRespDto.*;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/s/account")
    public ResponseEntity<?> saveAccount(@RequestBody @Valid AccountSaveReqDto dto,
                                         BindingResult bindingResult,
                                         @AuthenticationPrincipal LoginUser loginUser){
        AccountSaveRespDto accountSaveRespDto = accountService.계좌등록(dto, loginUser.getUser().getId());

        return new ResponseEntity<>(new ResponseDto<>(1,"계좌등록 성공",accountSaveRespDto), HttpStatus.CREATED);
    }
    
    @GetMapping("/s/account/login-user")
    public ResponseEntity<?> findUserAccount(@AuthenticationPrincipal LoginUser loginUser){
        AccountListRespDto accountListRespDto = accountService.계좌목록보기_유저별(loginUser.getUser().getId());

        return new ResponseEntity<>(new ResponseDto<>(1,"유저별 계좌목록보기 성공",accountListRespDto),HttpStatus.OK);
    }

    @DeleteMapping("/s/account/{number}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long number, @AuthenticationPrincipal LoginUser loginUser){
        accountService.계좌삭제(number,loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1,"계좌삭제 성공",null),HttpStatus.OK);
    }
}
