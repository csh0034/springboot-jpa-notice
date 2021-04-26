package com.ask.sample.advice.exception;

import com.ask.sample.constant.ErrorCode;

public class BusinessException extends BaseException {

    private static final long serialVersionUID = 4241895961562971468L;

    public BusinessException(String message) {
        super(message, ErrorCode.INTERNAL_SERVER_ERROR);
    }

    public BusinessException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}