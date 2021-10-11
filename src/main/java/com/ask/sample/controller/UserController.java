package com.ask.sample.controller;

import com.ask.sample.service.UserService;
import com.ask.sample.vo.request.UserRequestVO;
import com.ask.sample.vo.response.UserResponseVO;
import com.ask.sample.vo.response.common.CommonResponseVO;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/users")
  public CommonResponseVO<UserResponseVO> addUser(@RequestBody @Valid UserRequestVO requestVO) {
    String id = userService.addUser(requestVO);
    UserResponseVO userResponseVO = userService.findUser(id);
    return CommonResponseVO.ok(userResponseVO);
  }

  @PutMapping("/users/{userId}")
  public CommonResponseVO<UserResponseVO> updateUser(@PathVariable String userId, @RequestBody @Valid UserRequestVO requestVO) {
    String id = userService.updateUser(userId, requestVO);
    UserResponseVO userResponseVO = userService.findUser(id);
    return CommonResponseVO.ok(userResponseVO);
  }

  @DeleteMapping("/users/{userId}")
  public CommonResponseVO<Void> deleteUser(@PathVariable String userId) {
    userService.deleteUser(userId);
    return CommonResponseVO.ok();
  }
}
