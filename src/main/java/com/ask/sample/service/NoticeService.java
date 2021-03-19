package com.ask.sample.service;

import com.ask.sample.domain.Attachment;
import com.ask.sample.domain.Notice;
import com.ask.sample.domain.User;
import com.ask.sample.repository.NoticeRepository;
import com.ask.sample.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${upload.dir}")
    String uploadDir;

    @Transactional
    public String save(String userId, String title, String content, MultipartFile... multipartFiles) {
        User user = userRepository.findByIdAndEnabledIsTrue(userId).orElseThrow(() -> new IllegalStateException("user null"));

        List<Attachment> attachments = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (multipartFile == null && multipartFile.isEmpty()) {
                continue;
            }
            attachments.add(Attachment.createAttachment(multipartFile));
        }

        Notice notice = Notice.createNotice(user, title, content, attachments.toArray(new Attachment[0]));

        noticeRepository.save(notice);

        return notice.getId();
    }
}
