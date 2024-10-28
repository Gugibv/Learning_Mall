package com.grey.elasticsearch.test.bean;

import lombok.Data;

import java.util.LinkedHashMap;
@Data
public class VaEvent {
    private String id;
    private long time;
    private String type;
    private String alertName;
    private String severity;
}
