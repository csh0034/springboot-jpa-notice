package com.ask.sample.service;

import com.ask.sample.config.SettingProperties;
import com.ask.sample.domain.Attachment;
import com.ask.sample.domain.Notice;
import com.ask.sample.domain.User;
import com.ask.sample.repository.NoticeRepository;
import com.ask.sample.repository.UserRepository;
import com.ask.sample.util.FileUtils;
import com.ask.sample.vo.request.NoticeRequestVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;
    private final SettingProperties settingProperties;

    @Transactional
    public String add(String userId, NoticeRequestVO noticeRequestVO) {
        User user = userRepository.findByIdAndEnabledIsTrue(userId).orElseThrow(() -> new IllegalStateException("user null"));

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
}