package com.ask.sample.controller;

import com.ask.sample.domain.User;
import com.ask.sample.service.NoticeService;
import com.ask.sample.util.SecurityUtils;
import com.ask.sample.vo.request.NoticeRequestVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping("/add")
    public ResponseEntity<Void> add(@Valid NoticeRequestVO noticeRequestVO) {
        User currentUser = SecurityUtils.getCurrentUser();
        noticeService.add(currentUser.getId(), noticeRequestVO);
        return ResponseEntity.ok().build();
    }
}