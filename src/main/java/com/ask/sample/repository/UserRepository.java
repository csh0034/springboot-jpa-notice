package com.ask.sample.repository;

import com.ask.sample.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

  boolean existsByEmail(String email);

  Optional<User> findByEmailAndEnabledIsTrue(String email);
}