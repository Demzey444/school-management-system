package com.niit.sms.model.enums;

public enum Grade {
    A("Excellent"),
    B("Very Good"),
    C("Good"),
    D("Fair"),
    E("Pass"),
    F("Fail");

    private final String defaultRemark;

    Grade(String defaultRemark) {
        this.defaultRemark = defaultRemark;
    }

    public String getDefaultRemark() {
        return defaultRemark;
    }
}
