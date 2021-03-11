package com.ask.sample.repository;

import com.ask.sample.constant.Role;
import com.ask.sample.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired UserRepository userRepository;
    @Autowired EntityManager em;

    @Test
    public void saveUser() {
        // GIVEN
        User user = User.createUser("loginId1", "password1", Role.ROLE_USER, "userNm1");

        // WHEN
        User createUser = userRepository.save(user);

        // THEN
        em.flush();
        assertThat(createUser.getId()).isNotNull();
        assertThat(user).isEqualTo(createUser);
    }
}