package com.ask.sample.config.security;

import com.ask.sample.constant.ResponseCode;
import com.ask.sample.util.ResponseUtils;
import com.ask.sample.vo.response.common.ExceptionResponseVO;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

@Slf4j
public class JwtExceptionHandler implements AccessDeniedHandler, AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
    handleException(response, e);
  }

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
    handleException(response, e);
  }

  private void handleException(HttpServletResponse response, RuntimeException e) throws IOException {
    log.error("forbidden : {}", e.getClass().getSimpleName());
    ResponseUtils.writeJson(response, ExceptionResponseVO.of(ResponseCode.FORBIDDEN));
  }
}
