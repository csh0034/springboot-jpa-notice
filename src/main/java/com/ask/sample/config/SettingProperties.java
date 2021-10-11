package com.ask.sample.config;

import com.ask.sample.util.StringUtils;
import java.io.File;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "setting")
public class SettingProperties {

  private static final String HOME_PREFIX = "~/";

  private String uploadDir;

  private String commonErrorPage;

  private String serverUrl;

  private String jwtSecret;

  public String getUploadDir() {
    if (StringUtils.startsWith(uploadDir, HOME_PREFIX)) {
      uploadDir = System.getProperty("user.home") + File.separator + StringUtils.removeStart(uploadDir, HOME_PREFIX);
    }
    return uploadDir;
  }
}