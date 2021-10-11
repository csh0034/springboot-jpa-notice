package com.ask.sample.util;

import com.ask.sample.config.security.JwtUser;
import com.ask.sample.constant.Constants;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;

public final class JwtUtils {

  private static final String USER_ID_KEY = "userId";
  private static final String EMAIL_KEY = "email";
  private static final String AUTHORITY_KEY = "authority";

  private final Algorithm algorithm;
  private final JWTVerifier jwtVerifier;

  public JwtUtils(String jwtSecret) {
    this.algorithm = Algorithm.HMAC512(jwtSecret);
    this.jwtVerifier = JWT.require(algorithm).acceptLeeway(10).build();
  }

  public String generate(JwtUser jwtUser) {
    Date now = new Date();
    Date expiresAt = DateUtils.addDays(now, Constants.JWT_EXPIRATION_DAY);

    String token = JWT.create()
        .withClaim(USER_ID_KEY, jwtUser.getUserId())
        .withClaim(EMAIL_KEY, jwtUser.getEmail())
        .withClaim(AUTHORITY_KEY, jwtUser.getAuthority())
        .withExpiresAt(expiresAt)
        .withIssuedAt(now)
        .sign(algorithm);

    return Constants.JWT_TOKEN_PREFIX + token;
  }

  public JwtUser decode(String token) throws JWTVerificationException, IllegalArgumentException {
    token = StringUtils.replace(token, Constants.JWT_TOKEN_PREFIX, StringUtils.EMPTY);

    jwtVerifier.verify(token);

    DecodedJWT jwt = JWT.decode(token);
    String userId = jwt.getClaim(USER_ID_KEY).asString();
    String email = jwt.getClaim(EMAIL_KEY).asString();
    String authority = jwt.getClaim(AUTHORITY_KEY).asString();

    return JwtUser.of(userId, email, authority);
  }
}