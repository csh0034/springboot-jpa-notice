package com.ask.sample.advice;

import com.ask.sample.advice.exception.BaseException;
import com.ask.sample.advice.exception.InvalidationException;
import com.ask.sample.config.SettingProperties;
import com.ask.sample.constant.ErrorCode;
import com.ask.sample.util.StringUtils;
import com.ask.sample.vo.response.ExceptionResponseVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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
        ExceptionResponseVO responseVO = ExceptionResponseVO.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
        return getModelAndView(responseVO, ErrorCode.INVALID_INPUT_VALUE.getStatus());
    }

    @ExceptionHandler(InvalidationException.class)
    public ModelAndView handleInvalidationException(InvalidationException e) {
        log.error("handleInvalidationException", e);
        ErrorCode errorCode = e.getErrorCode();
        ExceptionResponseVO responseVO = ExceptionResponseVO.of(errorCode, e.getFieldErrors());
        return getModelAndView(responseVO, errorCode.getStatus());
    }

    @ExceptionHandler(BaseException.class)
    public ModelAndView handleBusinessException(BaseException e) {
        log.error("handleBusinessExceptionHandler", e);
        ErrorCode errorCode = e.getErrorCode();
        ExceptionResponseVO responseVO = ExceptionResponseVO.of(errorCode);
        return getModelAndView(responseVO, errorCode.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception e) {
        log.error("handleException", e);
        ExceptionResponseVO responseVO = ExceptionResponseVO.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return getModelAndView(responseVO, ErrorCode.INTERNAL_SERVER_ERROR.getStatus());
    }

    private ModelAndView getModelAndView(ExceptionResponseVO responseVO, HttpStatus status) {
        if (StringUtils.containsAny(request.getHeader(HttpHeaders.ACCEPT), MediaType.TEXT_HTML_VALUE, MediaType.ALL_VALUE)) {
            return new ModelAndView(
                settingProperties.getCommonErrorPage(),
                objectMapper.convertValue(responseVO, new TypeReference<Map<String, Object>>() {})
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