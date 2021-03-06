package com.ask.sample.advice.exception;

import com.ask.sample.constant.ResponseCode;
import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {

  private static final long serialVersionUID = 3166170761941160124L;

  private final ResponseCode responseCode;

  public BaseException(String message, ResponseCode responseCode) {
    super(message);
    this.responseCode = responseCode;
  }
}