package com.ask.sample.util;

import com.ask.sample.advice.exception.InvalidationException;
import com.ask.sample.domain.User;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@NoArgsConstructor(access = PRIVATE)
public final class SecurityUtils {

    private static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public static User getCurrentUser() {
        SecurityContext ctx = SecurityContextHolder.getContext();

        if (ctx.getAuthentication() == null) {
            throw new InvalidationException("User not found");
        }

        return (User) ctx.getAuthentication().getPrincipal();
    }

    public static boolean isAuthenticated() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication authentication = ctx.getAuthentication();

        if (authentication == null) {
            return false;
        }

        return ctx.getAuthentication() instanceof UsernamePasswordAuthenticationToken;
    }

    public static String passwordEncode(CharSequence rawPassword) {
        return PASSWORD_ENCODER.encode(rawPassword);
    }

    public static boolean passwordMatches(CharSequence rawPassword, String prefixEncodedPassword) {
        return PASSWORD_ENCODER.matches(rawPassword, prefixEncodedPassword);
    }

}
