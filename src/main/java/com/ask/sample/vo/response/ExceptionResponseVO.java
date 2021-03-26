package com.ask.sample.vo.response;

import com.ask.sample.constant.Constant;
import com.ask.sample.constant.ErrorCode;
import com.ask.sample.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PROTECTED;

@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
public class ExceptionResponseVO {

    private String timestamp;

    private String code;

    private String message;

    @JsonInclude(value = Include.NON_NULL)
    private List<FieldError> fieldErrors;

    private ExceptionResponseVO(ErrorCode errorCode, List<FieldError> fieldErrors) {
        this.timestamp = DateUtils.formatNow(Constant.DATE_FORMAT);
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.fieldErrors = fieldErrors;
    }

    private ExceptionResponseVO(ErrorCode errorCode) {
        this.timestamp = DateUtils.formatNow(Constant.DATE_FORMAT);
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public static ExceptionResponseVO of(ErrorCode errorCode, BindingResult bindingResult) {
        return new ExceptionResponseVO(errorCode, FieldError.of(bindingResult));
    }

    public static ExceptionResponseVO of(ErrorCode errorCode) {
        return new ExceptionResponseVO(errorCode);
    }

    public static ExceptionResponseVO of(ErrorCode errorCode, List<FieldError> fieldErrors) {
        return new ExceptionResponseVO(errorCode, fieldErrors);
    }

    @Getter
    @ToString
    @NoArgsConstructor(access = PROTECTED)
    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        private FieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static FieldError of(String field, String value, String reason) {
            return new FieldError(field, value, reason);
        }

        public static List<FieldError> of(BindingResult bindingResult) {
            return bindingResult.getFieldErrors().stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }
}
