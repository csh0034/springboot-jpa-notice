package com.ask.sample.repository;

import com.ask.sample.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    List<User> findAllByLoginId(String loginId);
}
