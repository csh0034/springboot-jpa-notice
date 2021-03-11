package com.ask.sample.service;

import com.ask.sample.constant.Role;
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
        User user = createUser();

        // WHEN
        String id = userService.join(user);

        // THEN
        em.flush();
        assertThat(user).isEqualTo(userRepository.findById(id).get());
    }

    @Test
    public void throwDuplicateUser() throws Exception {
        // GIVEN
        User user1 = createUser();
        User user2 = createUser();

        // WHEN
        userService.join(user1);

        // THEN
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() -> userService.join(user2));
    }

    private User createUser() {
        return User.createUser("loginId1", "password1", Role.ROLE_USER, "userNm1");
    }
}