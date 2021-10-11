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

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return userRepository.findByEmailAndEnabledIsTrue(email).orElseThrow(() -> new UsernameNotFoundException("NOT_FOUND"));
  }

  public String addUser(UserRequestVO userRequestVO) {
    User user = userRequestVO.toEntity();
    validateDuplicateMember(user.getEmail());
    userRepository.save(user);
    return user.getId();
  }

  private void validateDuplicateMember(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new BusinessException(ResponseCode.EMAIL_DUPLICATED);
    }
  }

  public String updateUser(String userId, UserRequestVO requestVO) {
    User user = getEnabledUser(userId);
    user.update(requestVO.getEmail(), requestVO.getPassword(), requestVO.getName());
    return user.getId();
  }

  private User getEnabledUser(String userId) {
    return userRepository.findByIdAndEnabledIsTrue(userId)
        .orElseThrow(() -> new EntityNotFoundException("user not found : " + userId));
  }

  public UserResponseVO findUser(String userId) {
    return UserResponseVO.of(getEnabledUser(userId));
  }

  public void deleteUser(String userId) {
    User user = getEnabledUser(userId);
    userRepository.delete(user);
  }
}
