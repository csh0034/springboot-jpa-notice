package com.ask.sample.controller;

import com.ask.sample.service.UserService;
import com.ask.sample.vo.request.UserRequestVO;
import com.ask.sample.vo.response.UserResponseVO;
import com.ask.sample.vo.response.common.CommonResponseVO;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/users")
  public CommonResponseVO<UserResponseVO> addUser(@Valid UserRequestVO requestVO) {
    String userId = userService.addUser(requestVO.toEntity());
    UserResponseVO userResponseVO = userService.findUser(userId);
    return CommonResponseVO.ok(userResponseVO);
  }
}
