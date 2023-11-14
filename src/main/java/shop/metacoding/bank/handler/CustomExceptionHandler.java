package shop.metacoding.bank.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shop.metacoding.bank.dto.ResponseDto;
import shop.metacoding.bank.handler.ex.CustomApiException;
import shop.metacoding.bank.handler.ex.CustomValidationException;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<?> apiException(CustomApiException e){
        log.error(e.getMessage());

        return new ResponseEntity<>(new ResponseDto<>(-1,e.getMessage(),null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<?> validationException(CustomValidationException e){
        log.error(e.getMessage());

        return new ResponseEntity<>(new ResponseDto<>(-1,e.getMessage(),e.getErrorMap()), HttpStatus.BAD_REQUEST);
    }
}
