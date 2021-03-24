package com.ask.sample.controller;

import com.ask.sample.advice.exception.BindingException;
import com.ask.sample.advice.exception.ServerException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NoticeController {

    @GetMapping("/test/error1")
    public void error1() {
        throw new BindingException("empty");
    }

    @GetMapping("/test/error2")
    public void error2() {
        BindingException ies = new BindingException();
        ies.add("field1", "에러 테스트1");
        ies.add("field2", "에러 테스트2");
        ies.add("에러 테스트2");

        if (ies.size() > 0) {
            throw ies;
        }
    }

    @GetMapping("/test/error3")
    public void error3() {
        throw new ServerException("abc");
    }
}
