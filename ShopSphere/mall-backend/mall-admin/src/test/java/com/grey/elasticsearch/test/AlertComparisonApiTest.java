package com.grey.elasticsearch.test;

import com.grey.elasticsearch.test.bean.AlarmRating;
import com.grey.elasticsearch.test.bean.AlarmRatingVO;
import com.grey.elasticsearch.test.constant.TimeInterval;
import com.grey.elasticsearch.test.utils.AlertComparisonApi;
import com.grey.elasticsearch.test.utils.ElasticsearchClient;

import java.util.HashMap;
import java.util.Map;

public class AlertComparisonApiTest {
    public static void main(String []args){
        co.elastic.clients.elasticsearch.ElasticsearchClient client = ElasticsearchClient.createLocalElasticsearchClientNoPWD();

        //  queryAllEventData(client);

        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("tenantId","e161c1d5d0c5ee1697c2abede4036f06");
        queryParameters.put("deviceChannelId","96ed11205ee2338179037eae0f3135f1");
        queryParameters.put("timeInterval", TimeInterval.MONTHLY);
        queryParameters.put("queryDate", "2024-10-08");

        AlarmRatingVO alarmRatingResult = null;
        if(TimeInterval.MONTHLY.equals(queryParameters.get("timeInterval"))&& queryParameters.get("queryDate")!=null){
             alarmRatingResult = AlertComparisonApi.dailyEventCountRate(client,queryParameters);

        }

        if(TimeInterval.YEARLY.equals(queryParameters.get("timeInterval"))&& queryParameters.get("queryDate")!=null){
            alarmRatingResult = AlertComparisonApi.monthlyEventCountRate(client,queryParameters);
        }
        assert alarmRatingResult != null;
        printAlarmRatingVOData(alarmRatingResult);
        ElasticsearchClient.closeClient(client);
    }


    public static void printAlarmRatingVOData(AlarmRatingVO alarmRatingVO) {
        System.out.println("Start Date: " + alarmRatingVO.getStartDate());
        System.out.println("End Date: " + alarmRatingVO.getEndDate());

        for (Map.Entry<String, AlarmRating> entry : alarmRatingVO.getAlarmRaingMap().entrySet()) {
            System.out.println(entry.getKey() +"  本月event数量："+ entry.getValue().getAlarmEventCount() +"  去年这个月event数量："+ entry.getValue().getLastAlarmEventCount()+" 变化率："+entry.getValue().getCompareLastCount());
        }
    }
}
