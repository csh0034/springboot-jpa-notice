package com.ask.sample.domain;

import static lombok.AccessLevel.PROTECTED;

import com.ask.sample.constant.Constants.Role;
import com.ask.sample.util.IdGenerator;
import com.ask.sample.util.SecurityUtils;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class User extends BaseEntity implements UserDetails {

  private static final long serialVersionUID = -5694089828831086520L;

  @Id
  @GenericGenerator(
      name = "userIdGenerator",
      strategy = "com.ask.sample.util.IdGenerator",
      parameters = @Parameter(name = IdGenerator.PARAM_KEY, value = "user-")
  )
  @GeneratedValue(generator = "userIdGenerator")
  @Column(name = "user_id", length = 50)
  @EqualsAndHashCode.Include
  private String id;

  @Column(unique = true, nullable = false, length = 30)
  private String email;

  @Column(nullable = false, length = 100)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 15)
  private Role role;

  @Column(nullable = false, length = 20)
  private String name;

  @ColumnDefault("0")
  private boolean enabled;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return AuthorityUtils.createAuthorityList(role.name());
  }

  @Override
  public String getUsername() {
    return email;
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

  public static User create(String email, String rawPassword, Role role, String name) {
    User user = new User();
    user.email = email;
    user.password = user.encodePassword(rawPassword);
    user.role = role;
    user.name = name;
    user.enabled = true;
    return user;
  }

  public void update(String email, String rawPassword, String name) {
    this.email = email;
    this.password = encodePassword(rawPassword);
    this.name = name;
  }

  private String encodePassword(String rawPassword) {
    return SecurityUtils.passwordEncode(rawPassword);
  }
}