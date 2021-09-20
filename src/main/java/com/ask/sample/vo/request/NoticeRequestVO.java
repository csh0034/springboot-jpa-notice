package com.ask.sample.vo.request;

import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@ToString
public class NoticeRequestVO {

  @NotBlank
  private String title;

  @NotBlank
  private String content;

  private String noticeId;

  private List<MultipartFile> multipartFiles;
}