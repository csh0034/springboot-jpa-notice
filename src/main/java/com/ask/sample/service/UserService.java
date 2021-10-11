package com.ask.sample.service;

import com.ask.sample.advice.exception.BusinessException;
import com.ask.sample.advice.exception.EntityNotFoundException;
import com.ask.sample.constant.ResponseCode;
import com.ask.sample.domain.User;
import com.ask.sample.repository.UserRepository;
import com.ask.sample.vo.request.UserRequestVO;
import com.ask.sample.vo.response.UserResponseVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;

  public String addUser(User user) {
    validateDuplicateMember(user.getEmail());
    userRepository.save(user);
    return user.getId();
  }

  public UserResponseVO findUser(String userId) {
    User user = userRepository.findById(userId)
        .filter(User::isEnabled)
        .orElseThrow(() -> new EntityNotFoundException("user not found : " + userId));
    return UserResponseVO.of(user);
  }

  private void validateDuplicateMember(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new BusinessException(ResponseCode.EMAIL_DUPLICATED);
    }
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return userRepository.findByEmailAndEnabledIsTrue(email).orElseThrow(() -> new UsernameNotFoundException("NOT_FOUND"));
  }
}
