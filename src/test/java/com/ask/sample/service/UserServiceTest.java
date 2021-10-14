package com.ask.sample.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.ask.sample.advice.exception.BusinessException;
import com.ask.sample.constant.Constants.Role;
import com.ask.sample.domain.User;
import com.ask.sample.repository.UserRepository;
import com.ask.sample.util.SecurityUtils;
import com.ask.sample.vo.request.UserRequestVO;
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

  @Test
  void joinUser() {
    // given
    UserRequestVO requestVO = createUserRequestVO();

    // when
    String id = userService.addUser(requestVO);

    // then
    assertThat(userRepository.existsById(id)).isTrue();
  }

  @Test
  void throwDuplicateUser() {
    // given
    UserRequestVO requestVO1 = createUserRequestVO();
    UserRequestVO requestVO2 = createUserRequestVO();

    // when
    userService.addUser(requestVO1);

    // then
    assertThatExceptionOfType(BusinessException.class).isThrownBy(() -> userService.addUser(requestVO2));
  }

  private UserRequestVO createUserRequestVO() {
    UserRequestVO requestVO = new UserRequestVO();
    requestVO.setEmail("sample01@gmail.com");
    requestVO.setPassword("sample-password");
    requestVO.setName("sample-name");
    return requestVO;
  }
}