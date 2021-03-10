package com.ask.sample.domain;

import com.ask.sample.constant.Role;
import com.ask.sample.util.IdGenerator;
import com.ask.sample.util.StringUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import javax.persistence.*;

@Entity
@Table(name = "comt_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseEntity {

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

    private String userNm;

    public static User createUser(String loginId, String password, Role authority, String userNm) {

        if (StringUtils.isBlank(loginId)) {
            throw new IllegalArgumentException("loginId must not be null");
        }
        if (StringUtils.isBlank(password)) {
            throw new IllegalArgumentException("password must not be null");
        }
        if (authority == null) {
            throw new IllegalArgumentException("authority must not be null");
        }

        User user = new User();
        user.loginId = loginId;
        user.password = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password);
        user.authority = authority;
        user.userNm = userNm;

        return user;
    }
}
