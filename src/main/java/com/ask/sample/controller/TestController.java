package com.ask.sample.controller;

import com.ask.sample.advice.exception.InvalidationException;
import com.ask.sample.advice.exception.BusinessException;
import com.ask.sample.constant.ErrorCode;
import com.ask.sample.vo.request.NoticeRequestVO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/test")
public class TestController {

    @GetMapping("/error1")
    public void error1() {
        throw new InvalidationException("메세세세세지지지지지지지지지지");
    }

    @GetMapping("/error2")
    public void error2() {
        InvalidationException ies = new InvalidationException();
        ies.add("field1", "value1", "에러 테스트1");
        ies.add("field2", "에러 테스트2");
        ies.add("에러 테스트3");

        if (ies.size() > 0) {
            throw ies;
        }
    }

    @GetMapping("/error3")
    public void error2(@Valid NoticeRequestVO noticeRequestVO, BindingResult bindingResult) {

        InvalidationException ies = new InvalidationException(bindingResult);
        ies.add("field1", "value1", "에러 테스트1");
        ies.add("field2", "에러 테스트2");
        ies.add("에러 테스트3");

        if (ies.size() > 0) {
            throw ies;
        }
    }

    @GetMapping("/error4")
    public void error4() {
        throw new BusinessException("error4");
    }

    @GetMapping("/error5")
    public void error5() {
        throw new BusinessException("error5", ErrorCode.ENTITY_NOT_FOUND);
    }

    @GetMapping("/authenticated")
    public void authentication() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication authentication = ctx.getAuthentication();
    }
}
