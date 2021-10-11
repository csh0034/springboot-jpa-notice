package com.ask.sample.repository;

import com.ask.sample.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

  Optional<User> findByEmailAndEnabledIsTrue(String id);

  Optional<User> findByIdAndEnabledIsTrue(String id);

  boolean existsByEmail(String email);

}