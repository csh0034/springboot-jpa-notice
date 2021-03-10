package com.ask.sample.advice;

import com.ask.sample.advice.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdvice {

    @ExceptionHandler(BusinessException.class)
    private ResponseEntity<?> businessExceptionHandler(BusinessException e) {
        log.error(e.getMessage(), e);
        return null;
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<?> exceptionHandler(BusinessException e) {
        log.error(e.getMessage(), e);
        return null;
    }
}
