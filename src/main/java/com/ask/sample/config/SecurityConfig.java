package com.ask.sample.config;

import com.ask.sample.config.security.JwtAuthenticationFilter;
import com.ask.sample.config.security.JwtExceptionHandler;
import com.ask.sample.config.security.JwtVerifyFilter;
import com.ask.sample.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final JwtUtils jwtUtils;

  @Override
  public void configure(WebSecurity web) {
    web.ignoring()
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
        .antMatchers("/error/**", "/h2-console/**", "/docs/index.html");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .headers().frameOptions().sameOrigin().and()
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .cors().and()
        .authorizeRequests()
          .antMatchers("/notices/**").hasRole("USER")
          //.antMatchers(HttpMethod.PUT, "/users").hasRole("USER")
          .anyRequest().permitAll().and()
        .exceptionHandling()
          .accessDeniedHandler(new JwtExceptionHandler())
          .authenticationEntryPoint(new JwtExceptionHandler()).and()
        .logout().disable()
        .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtUtils))
        .addFilter(new JwtVerifyFilter(jwtUtils));
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedOrigin("*");
    configuration.addAllowedHeader("*");
    configuration.addAllowedMethod("*");
    configuration.setMaxAge(1800L);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
