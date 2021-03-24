package com.ask.sample.vo.response;

import com.ask.sample.advice.exception.BindingException;
import com.ask.sample.advice.exception.BusinessException;
import com.ask.sample.constant.Code;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Date;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor
public class ExceptionResponseVO {

    private Date timestamp;

    private String code;

    private String message;

    @JsonInclude(value = Include.NON_NULL)
    private List<ResponseField> result;

    public static ExceptionResponseVO convert(BindingException e) {
        return new ExceptionResponseVO(new Date(), e.getCode(), e.getMessage(), e.getResponseMessages());
    }

    public static ExceptionResponseVO convert(BusinessException e) {
        return new ExceptionResponseVO(new Date(), e.getCode(), e.getMessage(), null);
    }

    public static ExceptionResponseVO convert(Exception e) {
        return new ExceptionResponseVO(new Date(), Code.SERVER_EXCEPTION, e.getMessage(), null);
    }

    @Getter
    @AllArgsConstructor(access = PRIVATE)
    public static class ResponseField {
        private String field;
        private String reason;

        public static ResponseField create(String reason) {
            return new ResponseField(null, reason);
        }
        public static ResponseField create(String field, String reason) {
            return new ResponseField(field, reason);
        }
    }
}
