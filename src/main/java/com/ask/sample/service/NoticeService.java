package com.ask.sample.service;

import com.ask.sample.advice.exception.InvalidationException;
import com.ask.sample.config.SettingProperties;
import com.ask.sample.domain.Attachment;
import com.ask.sample.domain.BaseEntity;
import com.ask.sample.domain.Notice;
import com.ask.sample.domain.User;
import com.ask.sample.repository.NoticeRepository;
import com.ask.sample.repository.UserRepository;
import com.ask.sample.util.FileUtils;
import com.ask.sample.vo.request.NoticeRequestVO;
import com.ask.sample.vo.response.AttachmentResponseVO;
import com.ask.sample.vo.response.NoticeResponseVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;
    private final SettingProperties settingProperties;

    @Transactional
    public String addNotice(String userId, NoticeRequestVO noticeRequestVO) {
        User user = userRepository.findByIdAndEnabledIsTrue(userId).orElseThrow(() -> new InvalidationException("user not found"));

        List<Attachment> attachments = new ArrayList<>();
        for (MultipartFile multipartFile : noticeRequestVO.getMultipartFiles()) {
            if (multipartFile == null || multipartFile.isEmpty()) {
                continue;
            }

            Attachment attachment = Attachment.createAttachment(multipartFile, settingProperties.getUploadDir());

            attachments.add(attachment);

            FileUtils.upload(multipartFile, settingProperties.getUploadDir(), attachment.getSavedFileDir());
        }

        Notice notice = Notice.createNotice(user, noticeRequestVO.getTitle(), noticeRequestVO.getContent(), attachments);

        noticeRepository.save(notice);

        return notice.getId();
    }

    public NoticeResponseVO findNotice(String noticeId) {

        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new InvalidationException("notice not found"));

        NoticeResponseVO responseVO = new NoticeResponseVO();
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
}