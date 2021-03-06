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

  private static final String ID_KEY = "id";
  private static final String LOGIN_ID_KEY = "loginId";
  private static final String AUTHORITY_KEY = "authority";

  private final Algorithm algorithm;
  private final JWTVerifier jwtVerifier;

  public JwtUtils(String jwtSecret) {
    this.algorithm = Algorithm.HMAC512(jwtSecret);
    this.jwtVerifier = JWT.require(algorithm).acceptLeeway(10).build();
  }

  public String generate(JwtUser jwtUser) {
    Date now = DateUtils.now();
    Date expiresAt = DateUtils.addDays(now, Constants.JWT_EXPIRATION_DAY);

    String token = JWT.create()
        .withClaim(ID_KEY, jwtUser.getId())
        .withClaim(LOGIN_ID_KEY, jwtUser.getLoginId())
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
    String id = jwt.getClaim(ID_KEY).asString();
    String loginId = jwt.getClaim(LOGIN_ID_KEY).asString();
    String authority = jwt.getClaim(AUTHORITY_KEY).asString();

    return JwtUser.of(id, loginId, authority);
  }
}