package shop.metacoding.bank.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shop.metacoding.bank.dto.ResponseDto;
import shop.metacoding.bank.handler.ex.CustomApiException;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<?> apiException(CustomApiException customApiException){
        log.error(customApiException.getMessage());

        return new ResponseEntity<>(new ResponseDto<>(-1,customApiException.getMessage(),null), HttpStatus.BAD_REQUEST);
    }
}
