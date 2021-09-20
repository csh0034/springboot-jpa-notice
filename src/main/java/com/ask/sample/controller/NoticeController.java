package com.ask.sample.controller;

import com.ask.sample.advice.exception.InvalidationException;
import com.ask.sample.config.security.JwtUser;
import com.ask.sample.service.NoticeService;
import com.ask.sample.util.SecurityUtils;
import com.ask.sample.util.StringUtils;
import com.ask.sample.vo.request.NoticeRequestVO;
import com.ask.sample.vo.response.NoticeResponseVO;
import com.ask.sample.vo.response.common.CommonPageResponseVO;
import com.ask.sample.vo.response.common.CommonResponseVO;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NoticeController {

  private final NoticeService noticeService;

  @PostMapping("/notice")
  public CommonResponseVO<NoticeResponseVO> addNotice(@Valid NoticeRequestVO noticeRequestVO) {
    JwtUser jwtUser = SecurityUtils.getCurrentJwtUser();
    String noticeId = noticeService.addNotice(jwtUser.getLoginId(), noticeRequestVO);
    NoticeResponseVO noticeResponseVO = noticeService.findNotice(noticeId, false);
    return CommonResponseVO.ok(noticeResponseVO);
  }

  @PostMapping("/notice/update")
  public CommonResponseVO<NoticeResponseVO> updateNotice(NoticeRequestVO noticeRequestVO, BindingResult bindingResult) {
    InvalidationException ies = new InvalidationException(bindingResult);

    if (StringUtils.isBlank(noticeRequestVO.getNoticeId())) {
      ies.add("noticeId", "noticeId must not be blank");
    }

    if (ies.size() > 0 || bindingResult.hasErrors()) {
      throw ies;
    }

    JwtUser jwtUser = SecurityUtils.getCurrentJwtUser();
    String noticeId = noticeService.updateNotice(jwtUser.getLoginId(), noticeRequestVO);
    NoticeResponseVO noticeResponseVO = noticeService.findNotice(noticeId, false);
    return CommonResponseVO.ok(noticeResponseVO);
  }

  @GetMapping("/notice/{noticeId}")
  public CommonResponseVO<NoticeResponseVO> findNotice(@PathVariable String noticeId) {
    NoticeResponseVO noticeResponseVO = noticeService.findNotice(noticeId, true);
    return CommonResponseVO.ok(noticeResponseVO);
  }

  @GetMapping("/notice")
  public CommonPageResponseVO<NoticeResponseVO> findNotices(String title, Pageable pageable) {
    Page<NoticeResponseVO> notices = noticeService.findNotices(title, pageable);
    return CommonPageResponseVO.ok(notices);
  }

  @GetMapping("/notice/{noticeId}/attachment/{attachmentId}")
  public void downloadAttachment(HttpServletResponse response, @PathVariable String noticeId, @PathVariable String attachmentId) {
    noticeService.downloadAttachment(response, noticeId, attachmentId);
  }

  @PostMapping("/notice/{noticeId}/attachment/{attachmentId}")
  public CommonResponseVO<Void> removeAttachment(@PathVariable String noticeId, @PathVariable String attachmentId) {
    noticeService.removeAttachment(noticeId, attachmentId);
    return CommonResponseVO.ok();
  }
}