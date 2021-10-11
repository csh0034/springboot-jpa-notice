package com.ask.sample.controller;

import com.ask.sample.config.security.JwtUser;
import com.ask.sample.service.NoticeService;
import com.ask.sample.util.SecurityUtils;
import com.ask.sample.vo.request.NoticeRequestVO;
import com.ask.sample.vo.response.NoticeResponseVO;
import com.ask.sample.vo.response.common.CommonPageResponseVO;
import com.ask.sample.vo.response.common.CommonResponseVO;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NoticeController {

  private final NoticeService noticeService;

  @PostMapping("/notices")
  public CommonResponseVO<NoticeResponseVO> addNotice(@Valid NoticeRequestVO noticeRequestVO) {
    JwtUser jwtUser = SecurityUtils.getCurrentJwtUser();
    String id = noticeService.addNotice(jwtUser.getUserId(), noticeRequestVO);
    NoticeResponseVO noticeResponseVO = noticeService.findNotice(id, false);
    return CommonResponseVO.ok(noticeResponseVO);
  }

  @PostMapping("/notices/{noticeId}")
  public CommonResponseVO<NoticeResponseVO> updateNotice(@PathVariable String noticeId, @Valid NoticeRequestVO noticeRequestVO) {
    JwtUser jwtUser = SecurityUtils.getCurrentJwtUser();
    String id = noticeService.updateNotice(jwtUser.getUserId(), noticeId, noticeRequestVO);
    NoticeResponseVO noticeResponseVO = noticeService.findNotice(id, false);
    return CommonResponseVO.ok(noticeResponseVO);
  }

  @GetMapping("/notices/{noticeId}")
  public CommonResponseVO<NoticeResponseVO> findNotice(@PathVariable String noticeId) {
    NoticeResponseVO noticeResponseVO = noticeService.findNotice(noticeId, true);
    return CommonResponseVO.ok(noticeResponseVO);
  }

  @GetMapping("/notices")
  public CommonPageResponseVO<NoticeResponseVO> findNotices(String title, Pageable pageable) {
    Page<NoticeResponseVO> notices = noticeService.findNotices(title, pageable);
    return CommonPageResponseVO.ok(notices);
  }

  @DeleteMapping("/notices/{noticeId}")
  public CommonResponseVO<Void> deleteNotice(@PathVariable String noticeId) {
    noticeService.deleteNotice(noticeId);
    return CommonResponseVO.ok();
  }

  @GetMapping("/notices/{noticeId}/attachments/{attachmentId}")
  public void downloadAttachment(HttpServletResponse response, @PathVariable String noticeId, @PathVariable String attachmentId) {
    noticeService.downloadAttachment(response, noticeId, attachmentId);
  }

  @DeleteMapping("/notices/{noticeId}/attachments/{attachmentId}")
  public CommonResponseVO<Void> deleteAttachment(@PathVariable String noticeId, @PathVariable String attachmentId) {
    noticeService.deleteAttachment(noticeId, attachmentId);
    return CommonResponseVO.ok();
  }
}