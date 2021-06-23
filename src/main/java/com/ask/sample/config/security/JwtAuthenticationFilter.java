package com.ask.sample.config.security;

import com.ask.sample.constant.Constant;
import com.ask.sample.constant.ResponseCode;
import com.ask.sample.domain.User;
import com.ask.sample.util.DateUtils;
import com.ask.sample.util.ResponseUtils;
import com.ask.sample.vo.response.common.CommonResponseVO;
import com.ask.sample.vo.response.common.ExceptionResponseVO;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String LOGIN_ID_KEY = "loginId";
    private static final String AUTHORITY_KEY = "authority";

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException {

        User user = (User) authResult.getPrincipal();

        String loginId = user.getLoginId();
        Date expiresAt = new Date(System.currentTimeMillis() + Constant.JWT_EXPIRATION_TIME);

        String token = JWT.create()
                .withClaim(LOGIN_ID_KEY, loginId)
                .withClaim(AUTHORITY_KEY, user.getAuthorityString())
                .withExpiresAt(expiresAt)
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC512(Constant.JWT_SECRET));

        Map<String, String> map = new HashMap<>();
        map.put(LOGIN_ID_KEY, loginId);
        map.put(AUTHORITY_KEY, DateUtils.formatDefault(expiresAt));
        map.put(Constant.JWT_HEADER_STRING, Constant.JWT_TOKEN_PREFIX + token);

        response.addHeader(Constant.JWT_HEADER_STRING, Constant.JWT_TOKEN_PREFIX + token);
        ResponseUtils.writeJson(response, CommonResponseVO.ok(map));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException {
        log.debug("authentication failed : {}", e.getClass().getSimpleName());
        ResponseUtils.writeJson(response, ExceptionResponseVO.of(ResponseCode.AUTHENTICATION_FAILED, e.getMessage()));
    }
}
