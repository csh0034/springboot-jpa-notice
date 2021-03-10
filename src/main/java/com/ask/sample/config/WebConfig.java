package com.ask.sample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.security.Principal;
import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public AuditorAware<String> auditorAware() {
       return () -> Optional.ofNullable(SecurityContextHolder.getContext())
               .map(SecurityContext::getAuthentication)
               .map(Principal::getName);
    }
}
