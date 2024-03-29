package com.ask.sample.vo.request;

import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class NoticeRequestVO {

  @NotBlank
  private String title;

  @NotBlank
  private String content;

  private List<MultipartFile> multipartFiles;
}