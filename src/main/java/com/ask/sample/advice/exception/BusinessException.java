package com.ask.sample.advice.exception;

import com.ask.sample.constant.ResponseCode;

public class BusinessException extends BaseException {

    private static final long serialVersionUID = 4241895961562971468L;

    public BusinessException(String message) {
        super(message, ResponseCode.INTERNAL_SERVER_ERROR);
    }

    public BusinessException(String message, ResponseCode responseCode) {
        super(message, responseCode);
    }
}