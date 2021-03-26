package com.ask.sample.service;

import com.ask.sample.constant.Constant;
import com.ask.sample.constant.Constant.Role;
import com.ask.sample.domain.User;
import com.ask.sample.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Transactional
@WithMockUser(username = "testUser")
public class UserServiceTest {

    @Autowired UserService userService;
    @Autowired UserRepository userRepository;
    @Autowired EntityManager em;

    @Test
    public void joinUser() {
        // GIVEN
        User user = createTestUser();

        // WHEN
        String id = userService.join(user);

        // THEN
        em.flush();
        assertThat(user).isEqualTo(userRepository.findById(id).get());
    }

    @Test
    public void throwDuplicateUser() throws Exception {
        // GIVEN
        User user1 = createTestUser();
        User user2 = createTestUser();

        // WHEN
        userService.join(user1);

        // THEN
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() -> userService.join(user2));
    }

    private User createTestUser() {
        return User.createUser("loginId1", "password1", Role.ROLE_USER, "userNm1");
    }
}