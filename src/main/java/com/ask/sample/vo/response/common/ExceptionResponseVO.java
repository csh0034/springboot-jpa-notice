package com.ask.sample.vo.response.common;

import static lombok.AccessLevel.PRIVATE;

import com.ask.sample.constant.Constants;
import com.ask.sample.constant.ResponseCode;
import com.ask.sample.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.validation.BindingResult;

@Getter
@ToString
@NoArgsConstructor(access = PRIVATE)
public class ExceptionResponseVO implements Serializable {

  private static final long serialVersionUID = 4569041055942536533L;

  private String timestamp;

  private String code;

  private String message;

  @JsonInclude(value = Include.NON_NULL)
  private List<CommonFieldError> fieldErrors;

  private ExceptionResponseVO(ResponseCode responseCode, List<CommonFieldError> fieldErrors) {
    this.timestamp = DateUtils.formatNow(Constants.DATE_FORMAT);
    this.code = responseCode.getCode();
    this.message = responseCode.getMessage();
    this.fieldErrors = fieldErrors;
  }

  private ExceptionResponseVO(ResponseCode responseCode, String message) {
    this.timestamp = DateUtils.formatNow(Constants.DATE_FORMAT);
    this.code = responseCode.getCode();
    this.message = message;
  }

  private ExceptionResponseVO(ResponseCode responseCode) {
    this.timestamp = DateUtils.formatNow(Constants.DATE_FORMAT);
    this.code = responseCode.getCode();
    this.message = responseCode.getMessage();
  }

  public static ExceptionResponseVO of(ResponseCode responseCode, BindingResult bindingResult) {
    return new ExceptionResponseVO(responseCode, CommonFieldError.of(bindingResult));
  }

  public static ExceptionResponseVO of(ResponseCode responseCode, String message) {
    return new ExceptionResponseVO(responseCode, message);
  }

  public static ExceptionResponseVO of(ResponseCode responseCode) {
    return new ExceptionResponseVO(responseCode);
  }

  public static ExceptionResponseVO of(ResponseCode responseCode, List<CommonFieldError> fieldErrors) {
    return new ExceptionResponseVO(responseCode, fieldErrors);
  }

  @Getter
  @ToString
  @NoArgsConstructor(access = PRIVATE)
  @AllArgsConstructor(access = PRIVATE)
  public static class CommonFieldError {

    private String field;
    private String value;
    private String reason;

    public static CommonFieldError of(String field, String value, String reason) {
      return new CommonFieldError(field, value, reason);
    }

    public static List<CommonFieldError> of(BindingResult bindingResult) {
      return bindingResult.getFieldErrors().stream()
          .map(error -> new CommonFieldError(
              error.getField(),
              error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
              error.getDefaultMessage()))
          .collect(Collectors.toList());
    }
  }
}
