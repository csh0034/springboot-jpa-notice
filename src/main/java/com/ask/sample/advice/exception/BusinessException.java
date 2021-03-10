package com.ask.sample.advice.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BusinessException extends RuntimeException {

    private String code;
    private String message;



    public BusinessException(String message, String code) {
        this.message = String.format("%s[%s]", message, code);
        this.code = code;
    }
}
