package com.ask.sample.config;

import com.ask.sample.util.JwtUtils;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Bean
  public JwtUtils jwtUtils(SettingProperties settingProperties) {
    return new JwtUtils(settingProperties.getJwtSecret());
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins("*")
        .allowedHeaders("*")
        .allowedMethods("*")
        .maxAge(1800L);
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {
    ApplicationConversionService.configure(registry);
  }
}