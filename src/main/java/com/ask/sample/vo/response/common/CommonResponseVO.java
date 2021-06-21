package com.ask.sample.vo.response.common;

import com.ask.sample.constant.Constant;
import com.ask.sample.constant.ResponseCode;
import com.ask.sample.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = PRIVATE)
public class CommonResponseVO<T> {

    private String timestamp;

    private String code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    private CommonResponseVO(ResponseCode responseCode, T result) {
        this.timestamp = DateUtils.formatNow(Constant.DATE_FORMAT.getValue());
        this.code = responseCode.getCode();
        this.result = result;
    }

    public static <T> CommonResponseVO<T> ok() {
        return new CommonResponseVO<>(ResponseCode.OK, null);
    }

    public static <T> CommonResponseVO<T> ok(T result) {
        return new CommonResponseVO<>(ResponseCode.OK, result);
    }
}