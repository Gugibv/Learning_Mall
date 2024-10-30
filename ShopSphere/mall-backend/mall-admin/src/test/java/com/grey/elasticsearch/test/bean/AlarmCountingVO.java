package com.grey.elasticsearch.test.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class AlarmCountingVO {
    private String deviceChannelId;
    private List<Map<String,Long>> countingList;
    private String startDate;
    private String endDate;
}
