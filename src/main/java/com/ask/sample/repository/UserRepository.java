package com.ask.sample.repository;

import com.ask.sample.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    List<User> findAllByLoginId(String loginId);

    Optional<User> findByLoginId(String loginId);

    Optional<User> findByIdAndEnabledIsTrue(String userId);
}