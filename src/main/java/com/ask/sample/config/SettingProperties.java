package com.ask.sample.config;

import com.ask.sample.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "setting")
public class SettingProperties {

    private String uploadDir;

    private String commonErrorPage;

    private String serverUrl;

    private String jwtSecret;

    public String getUploadDir() {
        final String homePrefix = "~/";
        if (StringUtils.startsWith(uploadDir, homePrefix)) {
            uploadDir = System.getProperty("user.home") +
                        System.getProperty("file.separator") +
                        StringUtils.removeStart(uploadDir, homePrefix);
        }
        return uploadDir;
    }
}