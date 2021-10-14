package com.ask.sample.vo.response;

import com.ask.sample.domain.User;
import com.ask.sample.util.DateUtils;
import com.ask.sample.util.StringUtils;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class UserResponseVO implements Serializable {

  private static final long serialVersionUID = 4076421376980429744L;

  private String id;

  private String email;

  private String name;

  private String createdBy;

  private LocalDateTime createdDt;

  public static UserResponseVO of(User user) {
    UserResponseVO vo = new UserResponseVO();
    vo.id = user.getId();
    vo.email = user.getEmail();
    vo.name = user.getName();
    vo.createdBy = user.getCreatedBy();
    vo.createdDt = user.getCreatedDt();
    return vo;
  }

  public String getCreatedBy() {
    return StringUtils.defaultString(createdBy);
  }

  public String getCreatedDt() {
    if (createdDt == null) {
      return "";
    }
    return createdDt.format(DateUtils.getDefaultTimeFormatter());
  }
}
