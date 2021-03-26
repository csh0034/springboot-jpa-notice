package com.ask.sample.advice.exception;

import com.ask.sample.constant.ErrorCode;

public abstract class BaseException extends RuntimeException {

    private static final long serialVersionUID = 3166170761941160124L;

    private ErrorCode errorCode;

    public BaseException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
