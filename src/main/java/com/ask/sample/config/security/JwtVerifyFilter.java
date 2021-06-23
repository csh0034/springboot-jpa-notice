package com.ask.sample.config.security;

import com.ask.sample.constant.Constants;
import com.ask.sample.constant.ResponseCode;
import com.ask.sample.util.JwtUtils;
import com.ask.sample.util.ResponseUtils;
import com.ask.sample.util.StringUtils;
import com.ask.sample.vo.response.common.ExceptionResponseVO;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtVerifyFilter extends BasicAuthenticationFilter {

    public JwtVerifyFilter() {
        super(authentication -> authentication);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String token = request.getHeader(Constants.JWT_HEADER_STRING);

        if (StringUtils.isBlank(token) || !StringUtils.startsWith(token, Constants.JWT_TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            JwtUser jwtUser = JwtUtils.decode(token);

            Authentication authentication = new UsernamePasswordAuthenticationToken(jwtUser, null, jwtUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);
        } catch (JWTVerificationException | IllegalArgumentException e) {
            log.error("jwt verify failed : ", e);
            ResponseUtils.writeJson(response, ExceptionResponseVO.of(ResponseCode.JWT_VERIFY_FAILED));
        }
    }
}
