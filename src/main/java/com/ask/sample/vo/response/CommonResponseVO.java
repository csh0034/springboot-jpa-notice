package com.ask.sample.vo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class CommonResponseVO<T> {

    private String timestamp;

    private int status;

    private T result;
}