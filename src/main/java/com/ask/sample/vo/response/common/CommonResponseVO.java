package com.ask.sample.vo.response.common;

import static lombok.AccessLevel.PRIVATE;

import com.ask.sample.constant.Constants;
import com.ask.sample.constant.ResponseCode;
import com.ask.sample.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = PRIVATE)
public class CommonResponseVO<T> implements Serializable {

  private static final long serialVersionUID = 6086380071497907730L;

  private String timestamp;

  private String code;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private T result;

  private CommonResponseVO(ResponseCode responseCode, T result) {
    this.timestamp = DateUtils.formatNow(Constants.DATE_FORMAT);
    this.code = responseCode.getCode();
    this.result = result;
  }

  public static <T> CommonResponseVO<T> ok() {
    return new CommonResponseVO<>(ResponseCode.OK, null);
  }

  public static <T> CommonResponseVO<T> ok(T result) {
    return new CommonResponseVO<>(ResponseCode.OK, result);
  }
}