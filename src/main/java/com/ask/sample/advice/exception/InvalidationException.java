package com.ask.sample.advice.exception;

import com.ask.sample.constant.ResponseCode;
import com.ask.sample.vo.response.common.ExceptionResponseVO.FieldError;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

public class InvalidationException extends BaseException {

    private static final long serialVersionUID = 7949500416132768977L;

    private List<FieldError> fieldErrors = new ArrayList<>();

    public InvalidationException() {
        super(ResponseCode.INVALID_INPUT_VALUE.getMessage(), ResponseCode.INVALID_INPUT_VALUE);
    }

    public InvalidationException(String message) {
        super(message, ResponseCode.INVALID_INPUT_VALUE);
    }

    public InvalidationException(BindingResult bindingResult) {
        super(ResponseCode.INVALID_INPUT_VALUE.getMessage(), ResponseCode.INVALID_INPUT_VALUE);
        this.fieldErrors = FieldError.of(bindingResult);
    }

    public void add(String reason) {
        add("", "", reason);
    }

    public void add(String field, String reason) {
        add(field, "", reason);
    }

    public void add(String field, String value, String reason) {
        fieldErrors.add(FieldError.of(field, value, reason));
    }

    public int size() {
        return fieldErrors.size();
    }

    public List<FieldError> getFieldErrors() {
        if (fieldErrors.isEmpty()) {
            add(super.getMessage());
        }
        return fieldErrors;
    }
}