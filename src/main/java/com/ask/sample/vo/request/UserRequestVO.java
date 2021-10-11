package com.ask.sample.vo.request;

import com.ask.sample.constant.Constants.Role;
import com.ask.sample.domain.User;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserRequestVO {

  @Pattern(regexp = "[a-zA-z0-9]+@[a-zA-z]+[.]+[a-zA-z.]+")
  @NotBlank
  @Size(max = 30)
  private String email;

  @NotBlank
  @Size(max = 100)
  private String password;

  @NotBlank
  @Size(max = 20)
  private String name;

  public User toEntity() {
    return User.create(email, password, Role.ROLE_USER, name);
  }
}
