package com.ask.sample.constant;

public class Constants {

  public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

  public static final int JWT_EXPIRATION_DAY = 1;
  public static final String JWT_TOKEN_PREFIX = "Bearer ";
  public static final String JWT_HEADER_STRING = "Authorization";

  public static final String EMAIL_PARAMETER = "email";
  public static final String PASSWORD_PARAMETER = "password";

  public enum Role {
    ROLE_USER
  }
}