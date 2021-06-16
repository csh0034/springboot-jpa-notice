package com.ask.sample.constant;

public enum Constant {

    DATE_FORMAT("yyyy-MM-dd HH:mm:ss");

    private final String value;

    Constant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public enum Role {
        ROLE_USER
    }
}