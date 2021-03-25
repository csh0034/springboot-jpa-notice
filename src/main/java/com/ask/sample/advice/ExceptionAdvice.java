package com.ask.sample.advice;

import com.ask.sample.advice.exception.BusinessException;
import com.ask.sample.advice.exception.InvalidationException;
import com.ask.sample.constant.ErrorCode;
import com.ask.sample.vo.response.ExceptionResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ExceptionResponseVO> handleBindException(BindException e) {
        log.error("handleBindException", e);
        ExceptionResponseVO response = ExceptionResponseVO.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
        return new ResponseEntity<>(response, ErrorCode.INVALID_INPUT_VALUE.getStatus());
    }

    @ExceptionHandler(InvalidationException.class)
    public ResponseEntity<ExceptionResponseVO> handleInvalidationException(InvalidationException e) {
        log.error("handleInvalidationException", e);
        ErrorCode errorCode = e.getErrorCode();
        ExceptionResponseVO response = ExceptionResponseVO.of(errorCode, e.getFieldErrors());
        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionResponseVO> handleBusinessException(BusinessException e) {
        log.error("handleBusinessExceptionHandler", e);
        ErrorCode errorCode = e.getErrorCode();
        ExceptionResponseVO response = ExceptionResponseVO.of(errorCode);
        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseVO> handleException(Exception e) {
        log.error("handleException", e);
        ExceptionResponseVO response = ExceptionResponseVO.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, ErrorCode.INTERNAL_SERVER_ERROR.getStatus());
    }
}
