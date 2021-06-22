package com.ask.sample.constant;

import org.springframework.http.HttpStatus;

public enum ResponseCode {

    OK(HttpStatus.OK, "40100", "OK"),

    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "40400", "Invalid Input Value"),
    ENTITY_NOT_FOUND(HttpStatus.BAD_REQUEST, "40401", "Entity Not Found"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "40999", "Internal Server Error"),

    AUTHENTICATION_FAILED(HttpStatus.BAD_REQUEST, "40701", "authentication failed");

    // EMAIL_DUPLICATION(HttpStatus.BAD_REQUEST, "U001", "Email is Duplication"),
    // LOGIN_INPUT_INVALID(HttpStatus.BAD_REQUEST, "U002", "Login input is invalid");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ResponseCode(final HttpStatus status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
