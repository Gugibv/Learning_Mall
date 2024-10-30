package com.grey.elasticsearch.test.bean;


import lombok.Data;

import java.util.List;

@Data
public class QueryParametersDTO {
    private String tenantId;
    private List<String> deviceChannelId;
    private String timeInterval;
    private String queryDate;
    private String startDate;
    private String endDate;
}
