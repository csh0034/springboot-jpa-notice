package com.ask.sample.vo.response;

import com.ask.sample.constant.Constant;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public abstract class BaseResponseVO {

    protected LocalDateTime createdDt;

    protected String createdBy;

    public String getCreatedDe() {
        if (createdDt == null) {
            return null;
        }
        return createdDt.format(DateTimeFormatter.ofPattern(Constant.DATE_FORMAT.getValue()));
    }
}