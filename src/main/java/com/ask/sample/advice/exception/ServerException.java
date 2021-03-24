package com.ask.sample.advice.exception;

import com.ask.sample.constant.Code;

public class ServerException extends BusinessException {

    private static final long serialVersionUID = 4241895961562971468L;

    public ServerException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return Code.SERVER_EXCEPTION;
    }
}
