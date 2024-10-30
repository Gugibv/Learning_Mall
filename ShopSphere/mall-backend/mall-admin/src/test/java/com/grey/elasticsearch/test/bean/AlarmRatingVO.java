package com.grey.elasticsearch.test.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class AlarmRatingVO {
    private String startDate;
    private String endDate;
    Map<String, AlarmRating> alarmRaingMap ;
}
