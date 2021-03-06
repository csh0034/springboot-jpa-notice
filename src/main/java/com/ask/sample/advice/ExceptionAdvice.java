package com.ask.sample.advice;

import com.ask.sample.advice.exception.BaseException;
import com.ask.sample.advice.exception.InvalidationException;
import com.ask.sample.config.SettingProperties;
import com.ask.sample.constant.ResponseCode;
import com.ask.sample.util.StringUtils;
import com.ask.sample.vo.response.common.ExceptionResponseVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdvice {

  private final HttpServletRequest request;
  private final ObjectMapper objectMapper;
  private final SettingProperties settingProperties;

  @ExceptionHandler(BindException.class)
  public ModelAndView handleBindException(BindException e) {
    log.error("handleBindException", e);
    ExceptionResponseVO responseVO = ExceptionResponseVO.of(ResponseCode.INVALID_INPUT_VALUE, e.getBindingResult());
    return getModelAndView(responseVO, ResponseCode.INVALID_INPUT_VALUE.getStatus());
  }

  @ExceptionHandler(InvalidationException.class)
  public ModelAndView handleInvalidationException(InvalidationException e) {
    log.error("handleInvalidationException", e);
    ResponseCode responseCode = e.getResponseCode();
    ExceptionResponseVO responseVO = ExceptionResponseVO.of(responseCode, e.getFieldErrors());
    return getModelAndView(responseVO, responseCode.getStatus());
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ModelAndView handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
    log.error("handleMethodArgumentTypeMismatchException", e);
    ExceptionResponseVO responseVO = ExceptionResponseVO.of(ResponseCode.METHOD_ARGUMENT_TYPE_MISMATCH);
    return getModelAndView(responseVO, ResponseCode.METHOD_ARGUMENT_TYPE_MISMATCH.getStatus());
  }

  @ExceptionHandler(BaseException.class)
  public ModelAndView handleBaseException(BaseException e) {
    log.error("handleBaseException", e);
    ResponseCode responseCode = e.getResponseCode();
    ExceptionResponseVO responseVO = ExceptionResponseVO.of(responseCode, e.getMessage());
    return getModelAndView(responseVO, responseCode.getStatus());
  }

  @ExceptionHandler(Exception.class)
  public ModelAndView handleException(Exception e) {
    log.error("handleException", e);
    ExceptionResponseVO responseVO = ExceptionResponseVO.of(ResponseCode.INTERNAL_SERVER_ERROR);
    return getModelAndView(responseVO, ResponseCode.INTERNAL_SERVER_ERROR.getStatus());
  }

  private ModelAndView getModelAndView(ExceptionResponseVO responseVO, HttpStatus status) {
    if (StringUtils.containsAny(request.getHeader(HttpHeaders.ACCEPT), MediaType.TEXT_HTML_VALUE, MediaType.ALL_VALUE)) {
      return new ModelAndView(
          settingProperties.getCommonErrorPage(),
          objectMapper.convertValue(responseVO, new TypeReference<Map<String, Object>>() {
          }),
          status
      );
    }

    MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
    jsonView.setExtractValueFromSingleKeyModel(true);

    ModelAndView modelAndView = new ModelAndView(jsonView);
    modelAndView.setStatus(status);
    modelAndView.addObject(responseVO);

    return modelAndView;
  }
}