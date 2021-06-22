package com.ask.sample.config.security;

import com.ask.sample.constant.Constant;
import com.ask.sample.constant.ResponseCode;
import com.ask.sample.domain.User;
import com.ask.sample.util.ResponseUtils;
import com.ask.sample.vo.response.common.ExceptionResponseVO;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {

        User user = (User) authResult.getPrincipal();

        String token = JWT.create()
                .withClaim("loginId", user.getLoginId())
                .withClaim("authority", user.getAuthorityString())
                .withExpiresAt(new Date(System.currentTimeMillis() + Constant.JWT_EXPIRATION_TIME))
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC512(Constant.JWT_SECRET));

        // TODO response 처리
        response.addHeader(Constant.JWT_HEADER_STRING, Constant.JWT_TOKEN_PREFIX + token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        ExceptionResponseVO responseVO = ExceptionResponseVO.of(ResponseCode.AUTHENTICATION_FAILED, failed.getMessage());
        ResponseUtils.writeJson(response, responseVO);
    }
}
