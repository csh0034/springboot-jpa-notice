package com.ask.sample.service;

import static java.util.stream.Collectors.toList;

import com.ask.sample.advice.exception.BusinessException;
import com.ask.sample.advice.exception.EntityNotFoundException;
import com.ask.sample.config.SettingProperties;
import com.ask.sample.domain.Attachment;
import com.ask.sample.domain.Notice;
import com.ask.sample.domain.User;
import com.ask.sample.repository.AttachmentRepository;
import com.ask.sample.repository.NoticeRepository;
import com.ask.sample.repository.UserRepository;
import com.ask.sample.util.FileUtils;
import com.ask.sample.util.StringUtils;
import com.ask.sample.vo.request.NoticeRequestVO;
import com.ask.sample.vo.response.NoticeResponseVO;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class NoticeService {

  private final NoticeRepository noticeRepository;
  private final AttachmentRepository attachmentRepository;
  private final UserRepository userRepository;
  private final SettingProperties settingProperties;

  public String addNotice(String loginId, NoticeRequestVO noticeRequestVO) {
    User user = getEnabledUser(loginId);

    List<Attachment> attachments = upload(noticeRequestVO.getMultipartFiles());

    Notice notice = Notice.create(user, noticeRequestVO.getTitle(), noticeRequestVO.getContent(), attachments);

    noticeRepository.save(notice);

    return notice.getId();
  }

  public String updateNotice(String loginId, NoticeRequestVO noticeRequestVO) {
    User user = getEnabledUser(loginId);

    Notice notice = getNotice(noticeRequestVO.getNoticeId());

    if (!StringUtils.equals(notice.getCreatedBy(), user.getId())) {
      throw new BusinessException("no permission to update");
    }

    List<Attachment> attachments = upload(noticeRequestVO.getMultipartFiles());

    notice.update(noticeRequestVO.getTitle(), noticeRequestVO.getContent(), attachments);

    return notice.getId();
  }

  private User getEnabledUser(String loginId) {
    return userRepository.findByLoginIdAndEnabledIsTrue(loginId)
        .orElseThrow(() -> new EntityNotFoundException("user not found"));
  }

  private Notice getNotice(String noticeId) {
    return noticeRepository.findById(noticeId)
        .orElseThrow(() -> new EntityNotFoundException("notice not found"));
  }

  private List<Attachment> upload(List<MultipartFile> multipartFiles) {
    if (multipartFiles == null) {
      return Collections.emptyList();
    }

    return multipartFiles.stream()
        .filter(multipartFile -> !multipartFile.isEmpty())
        .map(multipartFile -> {
          Attachment attachment = Attachment.create(multipartFile, settingProperties.getUploadDir());
          FileUtils.upload(multipartFile, settingProperties.getUploadDir(), attachment.getSavedFileDir());
          return attachment;
        })
        .collect(toList());
  }

  public NoticeResponseVO findNotice(String noticeId, boolean increaseReadCnt) {
    Notice notice = getNotice(noticeId);

    if (increaseReadCnt) {
      notice.increaseReadCnt();
    }

    return NoticeResponseVO.from(notice, settingProperties.getServerUrl());
  }

  public Page<NoticeResponseVO> findNotices(String title, Pageable pageable) {
    return noticeRepository.findNotices(title, pageable);
  }

  public void downloadAttachment(HttpServletResponse response, String noticeId, String attachmentId) {
    Attachment attachment = attachmentRepository.findAttachment(noticeId, attachmentId)
        .orElseThrow(() -> new EntityNotFoundException("attachment not found"));

    attachment.increaseDownloadCnt();

    FileUtils.downloadFile(response, attachment.getSavedFileDir(), attachment.getFileNm());
  }

  public void removeAttachment(String noticeId, String attachmentId) {
    Attachment attachment = attachmentRepository.findAttachment(noticeId, attachmentId)
        .orElseThrow(() -> new EntityNotFoundException("attachment not found"));

    FileUtils.removeFile(attachment.getSavedFileDir());
    attachmentRepository.delete(attachment);
  }
}