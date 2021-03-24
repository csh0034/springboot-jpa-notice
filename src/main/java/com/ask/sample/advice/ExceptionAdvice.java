package com.ask.sample.advice;

import com.ask.sample.advice.exception.BindingException;
import com.ask.sample.advice.exception.BusinessException;
import com.ask.sample.vo.response.ExceptionResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionResponseVO BusinessExceptionHandler(MethodArgumentNotValidException bindingException) {
        printLog(bindingException);
        return ExceptionResponseVO.convert(bindingException);
    }

    @ExceptionHandler(BusinessException.class)
    public ExceptionResponseVO businessExceptionHandler(BusinessException businessException) {
        printLog(businessException);
        return ExceptionResponseVO.convert(businessException);
    }

    @ExceptionHandler(Exception.class)
    public ExceptionResponseVO exceptionHandler(Exception exception) {
        printLog(exception);
        return ExceptionResponseVO.convert(exception);
    }

    private void printLog(Exception e) {
        log.error(e.getClass().getSimpleName(), e);
    }
}
