package com.ask.sample.advice.exception;

import com.ask.sample.constant.ResponseCode;

public class EntityNotFoundException extends BusinessException {

    private static final long serialVersionUID = -8477065927787135753L;

    public EntityNotFoundException(String message) {
        super(message, ResponseCode.ENTITY_NOT_FOUND);
    }
}
