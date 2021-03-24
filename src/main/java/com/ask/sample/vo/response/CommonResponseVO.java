package com.ask.sample.vo.response;

import java.util.Date;

public class CommonResponseVO<T> {

    private Date timestamp;

    private Integer status;

    private T result;
}
