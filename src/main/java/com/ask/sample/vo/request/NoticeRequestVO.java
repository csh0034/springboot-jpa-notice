package com.ask.sample.vo.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
public class NoticeRequestVO {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private MultipartFile[] multipartFiles;
}
