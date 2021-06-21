package com.ask.sample.controller;

import com.ask.sample.domain.User;
import com.ask.sample.service.NoticeService;
import com.ask.sample.util.SecurityUtils;
import com.ask.sample.vo.request.NoticeRequestVO;
import com.ask.sample.vo.response.common.CommonPageResponseVO;
import com.ask.sample.vo.response.common.CommonResponseVO;
import com.ask.sample.vo.response.NoticeResponseVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping("/notice")
    public CommonResponseVO<NoticeResponseVO> addNotice(@Valid NoticeRequestVO noticeRequestVO) {
        User currentUser = SecurityUtils.getCurrentUser();
        String noticeId = noticeService.addNotice(currentUser.getId(), noticeRequestVO);
        NoticeResponseVO noticeResponseVO = noticeService.findNotice(noticeId);
        return CommonResponseVO.ok(noticeResponseVO);
    }

    @GetMapping("/notice/{noticeId}")
    public CommonResponseVO<NoticeResponseVO> findNotice(@PathVariable String noticeId) {
        NoticeResponseVO noticeResponseVO = noticeService.findNotice(noticeId);
        return CommonResponseVO.ok(noticeResponseVO);
    }

    @GetMapping("/notice")
    public CommonPageResponseVO<NoticeResponseVO> findAllNotice(String title, Pageable pageable) {
        Page<NoticeResponseVO> noticeList = noticeService.findAllNotice(title, pageable);
        return CommonPageResponseVO.ok(noticeList);
    }
}