package com.ask.sample.config.security;

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

@Getter
@ToString
@NoArgsConstructor(access = PRIVATE)
public class JwtUser {

  private String loginId;

  private String authority;

  private JwtUser(String loginId, String authority) {
    this.loginId = loginId;
    this.authority = authority;
  }

  public static JwtUser of(String loginId, String authority) {
    return new JwtUser(loginId, authority);
  }

  @JsonIgnore
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return AuthorityUtils.createAuthorityList(authority);
  }
}
