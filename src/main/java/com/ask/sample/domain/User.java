package com.ask.sample.domain;

import static lombok.AccessLevel.PROTECTED;

import com.ask.sample.constant.Constants.Role;
import com.ask.sample.util.IdGenerator;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "tb_user")
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString
public class User extends BaseEntity implements UserDetails {

  private static final long serialVersionUID = -5694089828831086520L;

  @Id
  @GenericGenerator(
      name = "userIdGenerator",
      strategy = "com.ask.sample.util.IdGenerator",
      parameters = @Parameter(name = IdGenerator.PARAM_KEY, value = "user-")
  )
  @GeneratedValue(generator = "userIdGenerator")
  @Column(name = "user_id")
  private String id;

  @Column(unique = true, nullable = false)
  private String loginId;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Role authority;

  private String username;

  private boolean enabled;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return AuthorityUtils.createAuthorityList(authority.name());
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Transient
  public String getAuthorityString() {
    return authority.name();
  }

  public static User create(String loginId, String password, Role authority, String userNm) {
    User user = new User();
    user.loginId = loginId;
    user.password = password;
    user.authority = authority;
    user.username = userNm;
    user.enabled = true;
    return user;
  }

  public void changePassword(String newPassword) {
    this.password = newPassword;
  }
}