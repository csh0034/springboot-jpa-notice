package com.ask.sample.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ResponseCode {

  OK(HttpStatus.OK, "40100", "OK"),

  INVALID_INPUT_VALUE(HttpStatus.OK, "40400", "Invalid Input Value"),
  ENTITY_NOT_FOUND(HttpStatus.OK, "40401", "Entity Not Found"),
  METHOD_ARGUMENT_TYPE_MISMATCH(HttpStatus.OK, "40402", "Type Mismatch"),

  AUTHENTICATION_FAILED(HttpStatus.OK, "40501", "authentication failed"),
  JWT_VERIFY_FAILED(HttpStatus.OK, "40502", "jwt verify failed"),
  FORBIDDEN(HttpStatus.FORBIDDEN, "40503", "forbidden"),

  EMAIL_DUPLICATED(HttpStatus.OK, "40601", "email duplicated"),

  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "40999", "Internal Server Error");

  private final HttpStatus status;
  private final String code;
  private final String message;
}
