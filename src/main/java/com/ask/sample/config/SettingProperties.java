package com.ask.sample.config;

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
}