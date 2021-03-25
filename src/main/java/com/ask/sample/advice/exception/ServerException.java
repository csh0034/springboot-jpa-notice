package com.ask.sample.advice.exception;

import com.ask.sample.constant.ErrorCode;

public class ServerException extends BusinessException {

    private static final long serialVersionUID = 4241895961562971468L;

    public ServerException(String message) {
        super(message, ErrorCode.INTERNAL_SERVER_ERROR);
    }

    public ServerException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
