package com.grey.elasticsearch.test;

import com.grey.elasticsearch.test.bean.AlarmCountingVO;
import com.grey.elasticsearch.test.bean.QueryParametersDTO;
import com.grey.elasticsearch.test.constant.TimeInterval;
import com.grey.elasticsearch.test.utils.AlertTrendAnalysisApi;
import com.grey.elasticsearch.test.utils.ElasticsearchClient;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AlertTrendAnalysisApiTest {
    public static void main(String []args){
        co.elastic.clients.elasticsearch.ElasticsearchClient client = ElasticsearchClient.createLocalElasticsearchClientNoPWD();

        //  queryAllEventData(client);
        QueryParametersDTO queryParametersDTO = new QueryParametersDTO();
        queryParametersDTO .setTenantId("e161c1d5d0c5ee1697c2abede4036f06");
        List<String> channerIdList = new ArrayList<>();
        channerIdList.add("96ed11205ee2338179037eae0f3135f1");
        queryParametersDTO.setDeviceChannelId(channerIdList);
        queryParametersDTO.setTimeInterval(TimeInterval.MONTHLY.getDisplayName());
        queryParametersDTO.setQueryDate("2024-10-30");

         AlertTrendAnalysisApi.queryAllEventData(client,queryParametersDTO);

        List<AlarmCountingVO>  alarmCountingVOList = null;
        if(TimeInterval.DAY.getDisplayName().equals(queryParametersDTO.getTimeInterval())){
           alarmCountingVOList= AlertTrendAnalysisApi.queryHourlyEventCount(client,queryParametersDTO);

        }

        if(TimeInterval.WEEK.getDisplayName().equals(queryParametersDTO.getTimeInterval())){
            String queryDate =queryParametersDTO.getQueryDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate date = LocalDate.parse(queryDate, formatter);
            String startDate = date.with(DayOfWeek.MONDAY).toString();
            String endDate = date.with(DayOfWeek.SUNDAY).toString();

            System.out.println("本周开始日期 " +startDate);
            System.out.println("本周结束日期: " + endDate);
            queryParametersDTO.setStartDate(startDate);
            queryParametersDTO.setEndDate(endDate);
            alarmCountingVOList= AlertTrendAnalysisApi.queryDailyEventCount(client,queryParametersDTO);
        }

        if(TimeInterval.MONTHLY.getDisplayName().equals(queryParametersDTO.getTimeInterval())){
            String queryDate = queryParametersDTO.getQueryDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate date = LocalDate.parse(queryDate, formatter);
            String startDate  = date.with(TemporalAdjusters.firstDayOfMonth()).toString();
            String endDate = date.with(TemporalAdjusters.lastDayOfMonth()).toString();

            System.out.println("本月开始日期：" + startDate);
            System.out.println("本月结束日期：" + endDate);

            queryParametersDTO.setStartDate(startDate);
            queryParametersDTO.setEndDate(endDate);
            alarmCountingVOList= AlertTrendAnalysisApi.queryDailyEventCount(client,queryParametersDTO);
        }

        printAlarmCountingVOList(alarmCountingVOList);
        ElasticsearchClient.closeClient(client);



    }


    public static void printAlarmCountingVOList(List<AlarmCountingVO> alarmCountingVOList) {
        if (alarmCountingVOList == null || alarmCountingVOList.isEmpty()) {
            System.out.println("No data in alarmCountingVOList.");
            return;
        }

        for (AlarmCountingVO alarmCountingVO : alarmCountingVOList) {
            System.out.println("Device Channel ID: " + alarmCountingVO.getDeviceChannelId());
            System.out.println("Start Date: " + alarmCountingVO.getStartDate());
            System.out.println("End Date: " + alarmCountingVO.getEndDate());

            System.out.println("Counting List:");
            List<Map<String, Long>> countingList = alarmCountingVO.getCountingList();
            if (countingList != null) {
                for (Map<String, Long> map : countingList) {
                    map.forEach((key, value) -> System.out.println("    " + key + ": " + value));
                }
            } else {
                System.out.println("    No data in counting list.");
            }
            System.out.println("-----------------------------------");
        }
    }

}
