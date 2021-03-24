package com.ask.sample.advice.exception;

public abstract class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 3166170761941160124L;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }

    public abstract String getCode();
}
