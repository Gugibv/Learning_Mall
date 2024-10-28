package com.grey.elasticsearch.test.constant;

public enum TimeInterval {
    YEARLY("YEARLY"),
    MONTHLY("MONTHLY"),
    WEEK("WEEK"),
    DAY("DAY");

    private final String displayName;

    TimeInterval(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}