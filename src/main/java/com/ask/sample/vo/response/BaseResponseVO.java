package com.ask.sample.vo.response;

import com.ask.sample.constant.Constants;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseResponseVO {

  protected LocalDateTime createdDt;

  protected String createdBy;

  public String getCreatedDt() {
    if (createdDt == null) {
      return null;
    }
    return createdDt.format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT));
  }
}
