package com.ask.sample.service;

import com.ask.sample.advice.exception.EntityNotFoundException;
import com.ask.sample.config.SettingProperties;
import com.ask.sample.domain.Attachment;
import com.ask.sample.domain.BaseEntity;
import com.ask.sample.domain.Notice;
import com.ask.sample.domain.User;
import com.ask.sample.repository.AttachmentRepository;
import com.ask.sample.repository.NoticeRepository;
import com.ask.sample.repository.UserRepository;
import com.ask.sample.util.FileUtils;
import com.ask.sample.vo.request.NoticeRequestVO;
import com.ask.sample.vo.response.AttachmentResponseVO;
import com.ask.sample.vo.response.NoticeResponseVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final AttachmentRepository attachmentRepository;
    private final UserRepository userRepository;
    private final SettingProperties settingProperties;

    @Transactional
    public String addNotice(String loginId, NoticeRequestVO noticeRequestVO) {
        User user = userRepository.findByLoginIdAndEnabledIsTrue(loginId)
                .orElseThrow(() -> new EntityNotFoundException("user not found"));

        List<Attachment> attachments = new ArrayList<>();

        if (noticeRequestVO.getMultipartFiles() != null) {
            for (MultipartFile multipartFile : noticeRequestVO.getMultipartFiles()) {
                if (multipartFile == null || multipartFile.isEmpty()) {
                    continue;
                }

                Attachment attachment = Attachment.create(multipartFile, settingProperties.getUploadDir());

                attachments.add(attachment);

                FileUtils.upload(multipartFile, settingProperties.getUploadDir(), attachment.getSavedFileDir());
            }
        }

        Notice notice = Notice.create(user, noticeRequestVO.getTitle(), noticeRequestVO.getContent(), attachments);

        noticeRepository.save(notice);

        return notice.getId();
    }

    @Transactional
    public NoticeResponseVO findNotice(String noticeId, boolean increaseReadCnt) {

        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new EntityNotFoundException("notice not found"));

        if (increaseReadCnt) {
            notice.increaseReadCnt();
        }

        NoticeResponseVO responseVO = new NoticeResponseVO();
        responseVO.setFileCnt(notice.getAttachments().size());
        BeanUtils.copyProperties(notice, responseVO);

        List<AttachmentResponseVO> files = notice.getAttachments().stream()
                .sorted(Comparator.comparing(BaseEntity::getCreatedDt))
                .map(attachment -> {
                    AttachmentResponseVO attResponseVO = new AttachmentResponseVO();
                    attResponseVO.setFileUrl(
                        String.format("%s/notice/%s/attachment/%s",
                            settingProperties.getServerUrl(),
                            notice.getId(),
                            attachment.getId())
                    );

                    BeanUtils.copyProperties(attachment, attResponseVO);

                    return attResponseVO;
                }).collect(toList());

        responseVO.setFiles(files);

        return responseVO;
    }

    public Page<NoticeResponseVO> findAllNotice(String title, Pageable pageable) {
        return noticeRepository.findAllNotice(title, pageable);
    }

    @Transactional
    public void downloadAttachment(HttpServletResponse response, String noticeId, String attachmentId) {
        Attachment attachment = attachmentRepository.findAttachment(noticeId, attachmentId)
                .orElseThrow(() -> new EntityNotFoundException("attachment not found"));

        attachment.increaseDownloadCnt();

        FileUtils.downloadFile(response, attachment.getSavedFileDir(), attachment.getFileNm());
    }

    @Transactional
    public void remoteAttachment(String noticeId, String attachmentId) {
        Attachment attachment = attachmentRepository.findAttachment(noticeId, attachmentId)
                .orElseThrow(() -> new EntityNotFoundException("attachment not found"));

        FileUtils.removeFile(attachment.getSavedFileDir());
        attachmentRepository.delete(attachment);
    }
}