package com.ask.sample.config;

import com.ask.sample.util.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public JwtUtils jwtUtils(SettingProperties settingProperties) {
        return new JwtUtils(settingProperties.getJwtSecret());
    }
}