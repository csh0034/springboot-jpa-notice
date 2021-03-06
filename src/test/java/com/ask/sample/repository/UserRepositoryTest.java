package com.ask.sample.repository;

import static com.ask.sample.constant.Constants.Role;
import static org.assertj.core.api.Assertions.assertThat;

import com.ask.sample.common.TestConfig;
import com.ask.sample.domain.User;
import com.ask.sample.util.SecurityUtils;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(TestConfig.class)
class UserRepositoryTest {

  @Autowired
  UserRepository userRepository;

  @Autowired
  EntityManager em;

  @Test
  void saveUser() {
    // GIVEN
    User user = User.create("loginId1", SecurityUtils.passwordEncode("password1"), Role.ROLE_USER, "userNm1");

    // WHEN
    User createUser = userRepository.save(user);

    // THEN
    em.flush();
    assertThat(createUser.getId()).isNotNull();
    assertThat(user).isEqualTo(createUser);
  }
}