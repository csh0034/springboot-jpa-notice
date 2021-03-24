package com.ask.sample.advice.exception;

import com.ask.sample.constant.Code;
import com.ask.sample.vo.response.ExceptionResponseVO.ResponseField;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class BindingException extends BusinessException {

    private static final long serialVersionUID = -7907153421305096215L;

    private List<ResponseField> responseMessages = new ArrayList<>();

    public BindingException(String reason) {
        super(reason);
        responseMessages.add(ResponseField.create(reason));
    }

    public BindingException add(String reason) {
        responseMessages.add(ResponseField.create(reason));
        return this;
    }

    public BindingException add(String field, String reason) {
        responseMessages.add(ResponseField.create(field, reason));
        return this;
    }

    public int size() {
        return this.responseMessages.size();
    }

    @Override
    public String getCode() {
        return Code.BINDING_EXCEPTION;
    }
}
