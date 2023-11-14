package shop.metacoding.bank.handler.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import shop.metacoding.bank.handler.ex.CustomValidationException;

import java.util.HashMap;
import java.util.Map;

@Component
@Aspect
public class CustomValidationAdvice {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMapping(){}

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void putMapping(){}

    @Around("postMapping() || putMapping()") // joinPoint 의 전/후 제어가 된다.
    public Object validationAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs(); // joinPoint 의 매개변수
        for (Object arg : args) {
            if(arg instanceof BindingResult){ // 매개변수중 BindingResult 가 있다면(유효성검사를 하는 메서드)
                BindingResult bindingResult = (BindingResult) arg; // Object -> BindingResult 다운캐스팅

                if(bindingResult.hasErrors()){
                    Map<String, String> errorMap = new HashMap<>();

                    for (FieldError error : bindingResult.getFieldErrors()) {
                        errorMap.put(error.getField(), error.getDefaultMessage());
                    }
                    throw new CustomValidationException("유효성검사 실패", errorMap); // 직접 만든 예외처리 로직실행
                }
            }
        }
        return  joinPoint.proceed(); // 정상적으로 해당 메서드를 실행
    }
}
