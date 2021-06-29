package com.ask.sample.config.security;

import com.ask.sample.constant.Constants;
import com.ask.sample.constant.ResponseCode;
import com.ask.sample.domain.User;
import com.ask.sample.util.JwtUtils;
import com.ask.sample.util.ResponseUtils;
import com.ask.sample.vo.response.common.CommonResponseVO;
import com.ask.sample.vo.response.common.ExceptionResponseVO;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final JwtUtils jwtUtils;

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
    super(authenticationManager);
    this.jwtUtils = jwtUtils;
    this.setUsernameParameter(Constants.LOGIN_ID_PARAMETER);
    this.setPasswordParameter(Constants.PASSWORD_PARAMETER);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
      Authentication authResult)
      throws IOException {

    User user = (User) authResult.getPrincipal();

    JwtUser jwtUser = JwtUser.of(user.getLoginId(), user.getAuthorityString());
    String token = jwtUtils.generate(jwtUser);

    ResponseUtils.writeJson(response, CommonResponseVO.ok(token));
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
      throws IOException {
    log.error("authentication failed : ", e);
    ResponseUtils.writeJson(response, ExceptionResponseVO.of(ResponseCode.AUTHENTICATION_FAILED));
  }
}
