package com.ask.sample.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.ask.sample.advice.exception.BusinessException;
import com.ask.sample.constant.Constants.Role;
import com.ask.sample.domain.User;
import com.ask.sample.repository.UserRepository;
import com.ask.sample.util.SecurityUtils;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserServiceTest {

  @Autowired
  UserService userService;

  @Autowired
  UserRepository userRepository;

  @Autowired
  EntityManager em;

  @Test
  void joinUser() {
    // GIVEN
    User user = createTestUser();

    // WHEN
    String id = userService.addUser(user);

    // THEN
    em.flush();
    assertThat(user).isEqualTo(userRepository.findById(id).get());
  }

  @Test
  void throwDuplicateUser() {
    // GIVEN
    User user1 = createTestUser();
    User user2 = createTestUser();

    // WHEN
    userService.addUser(user1);

    // THEN
    assertThatExceptionOfType(BusinessException.class).isThrownBy(() -> userService.addUser(user2));
  }

  private User createTestUser() {
    return User.create("user01@rsupport.com", SecurityUtils.passwordEncode("password1"), Role.ROLE_USER, "userNm1");
  }
}