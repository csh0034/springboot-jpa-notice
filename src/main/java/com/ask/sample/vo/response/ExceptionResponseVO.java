package com.ask.sample.vo.response;

import com.ask.sample.constant.Constant;
import com.ask.sample.constant.ResponseCode;
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

    private ExceptionResponseVO(ResponseCode responseCode, List<FieldError> fieldErrors) {
        this.timestamp = DateUtils.formatNow(Constant.DATE_FORMAT.getValue());
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
        this.fieldErrors = fieldErrors;
    }

    private ExceptionResponseVO(ResponseCode responseCode) {
        this.timestamp = DateUtils.formatNow(Constant.DATE_FORMAT.getValue());
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
    }

    public static ExceptionResponseVO of(ResponseCode responseCode, BindingResult bindingResult) {
        return new ExceptionResponseVO(responseCode, FieldError.of(bindingResult));
    }

    public static ExceptionResponseVO of(ResponseCode responseCode) {
        return new ExceptionResponseVO(responseCode);
    }

    public static ExceptionResponseVO of(ResponseCode responseCode, List<FieldError> fieldErrors) {
        return new ExceptionResponseVO(responseCode, fieldErrors);
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
