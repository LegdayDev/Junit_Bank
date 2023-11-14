package shop.metacoding.bank.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.metacoding.bank.dto.ResponseDto;
import shop.metacoding.bank.service.UserService;

import javax.validation.Valid;

import java.util.HashMap;
import java.util.Map;

import static shop.metacoding.bank.dto.user.UserReqDto.*;
import static shop.metacoding.bank.dto.user.UserRespDto.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Valid JoinReqDto joinReqDto, BindingResult  bindingResult){
        if(bindingResult.hasErrors()){
            Map<String, String> errorMap = new HashMap<>();

            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(new ResponseDto<>(-1,"유효성 검사 실패",errorMap),HttpStatus.BAD_REQUEST);
        }

        JoinRespDto joinRespDto = userService.회원가입(joinReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1,"회원가입 완료",joinRespDto), HttpStatus.CREATED);
    }
}
