package com.ask.sample.repository;

import static com.ask.sample.constant.Constants.Role;
import static org.assertj.core.api.Assertions.assertThat;

import com.ask.sample.common.QuerydslConfig;
import com.ask.sample.domain.User;
import com.ask.sample.util.SecurityUtils;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(QuerydslConfig.class)
class UserRepositoryTest {

  @Autowired
  UserRepository userRepository;

  @Autowired
  EntityManager em;

  @Test
  void saveUser() {
    // given
    User user = User.create("user01@rsupport.com", SecurityUtils.passwordEncode("password1"), Role.ROLE_USER, "userNm1");

    // when
    User createUser = userRepository.save(user);

    // then
    em.flush();
    assertThat(createUser.getId()).isNotNull();
    assertThat(user).isEqualTo(createUser);
  }
}