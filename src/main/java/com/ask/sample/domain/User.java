package com.ask.sample.domain;

import com.ask.sample.constant.Role;
import com.ask.sample.util.IdGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "comt_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseEntity implements UserDetails {

    @Id
    @GenericGenerator(
        name = "userGrpIdGenerator",
        strategy = "com.ask.sample.util.IdGenerator",
        parameters = @Parameter(name = IdGenerator.PARAM_KEY, value = "user-")
    )
    @GeneratedValue(generator = "userGrpIdGenerator")
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

    public static User createUser(String loginId, String password, Role authority, String userNm) {
        User user = new User();
        user.loginId = loginId;
        user.password = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password);
        user.authority = authority;
        user.username = userNm;
        user.enabled = true;
        return user;
    }

    // 비지니스 로직
    public void changePassword(String oldPassword, String newPassword) {
        this.password = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(newPassword);
    }

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
}
