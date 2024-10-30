package com.grey.elasticsearch.test.bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AlarmRating {
    private Long lastAlarmEventCount;
    private Long alarmEventCount ;
    private Double compareLastCount ;
}
