package com.ask.sample.service;

import com.ask.sample.advice.exception.InvalidationException;
import com.ask.sample.domain.User;
import com.ask.sample.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional
    public String join(User user) {

        validateDuplicateMember(user);

        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateMember(User user) {
        List<User> findUsers = userRepository.findAllByLoginId(user.getLoginId());
        if (!findUsers.isEmpty()) {
            throw new InvalidationException("user already exists");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        return userRepository.findByLoginIdAndEnabledIsTrue(loginId).orElseThrow(() -> new UsernameNotFoundException("NOT_FOUND"));
    }
}
