package com.grey.elasticsearch.test.utils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.CalendarInterval;
import co.elastic.clients.elasticsearch._types.aggregations.DateHistogramAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import com.grey.elasticsearch.test.bean.AlarmRating;
import com.grey.elasticsearch.test.bean.AlarmRatingVO;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class AlertComparisonApi {

    public static AlarmRatingVO dailyEventCountRate(ElasticsearchClient client, Map<String, Object> queryParameters){
        AlarmRatingVO alarmRatingResultVO = new AlarmRatingVO();

        String queryDate = queryParameters.get("queryDate").toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate date = LocalDate.parse(queryDate, formatter);
        String startDate  = date.with(TemporalAdjusters.firstDayOfMonth()).toString();
        String endDate = date.with(TemporalAdjusters.lastDayOfMonth()).toString();
        alarmRatingResultVO.setStartDate(startDate);
        alarmRatingResultVO.setEndDate(endDate);

        queryParameters.put("startDate",startDate);
        queryParameters.put("endDate",endDate);
        HashMap<String, Long> eventCountMap = queryDailyEventCount(client,queryParameters);

        LocalDate previousMonth = date.minusMonths(1);
        String lastMonthStartDate = previousMonth.with(TemporalAdjusters.firstDayOfMonth()).toString();
        String lastMonthEndDate = previousMonth.with(TemporalAdjusters.lastDayOfMonth()).toString();

        queryParameters.put("startDate",lastMonthStartDate);
        queryParameters.put("endDate",lastMonthEndDate);
        HashMap<String, Long> lastMonthEventCountMap = queryDailyEventCount(client,queryParameters);
        Map<String, AlarmRating>  alarmCountingHashMap = new HashMap<>();

        if(eventCountMap.isEmpty() && !lastMonthEventCountMap.isEmpty()){
            lastMonthEventCountMap.forEach((key, value) -> {
                AlarmRating alarmRating = new AlarmRating();
                alarmRating.setAlarmEventCount(0L);
                alarmRating.setLastAlarmEventCount(value);
                alarmRating.setCompareLastCount(getChangeRate( alarmRating.getAlarmEventCount(),alarmRating.getLastAlarmEventCount()));
                alarmCountingHashMap.put(key, alarmRating);
                alarmRatingResultVO.setAlarmRaingMap(alarmCountingHashMap);
            });

            return alarmRatingResultVO;
        }

        if(!eventCountMap.isEmpty() && lastMonthEventCountMap.isEmpty()){
            eventCountMap.forEach((key, value) -> {
                AlarmRating alarmRating = new AlarmRating();
                alarmRating.setAlarmEventCount(value);
                alarmRating.setLastAlarmEventCount(0L);
                alarmRating.setCompareLastCount(getChangeRate( alarmRating.getAlarmEventCount(),alarmRating.getLastAlarmEventCount()));
                alarmCountingHashMap.put(key, alarmRating);
                alarmRatingResultVO.setAlarmRaingMap(alarmCountingHashMap);
            });

            return alarmRatingResultVO;
        }


        for (String dateStr : eventCountMap.keySet()) {
            if (lastMonthEventCountMap.containsKey(dateStr)) {
                Long currentMonthCount = eventCountMap.get(dateStr);
                Long lastMonthCount = lastMonthEventCountMap.get(dateStr);

                AlarmRating alarmRating = new AlarmRating();
                alarmRating.setAlarmEventCount(currentMonthCount);
                alarmRating.setLastAlarmEventCount(lastMonthCount);
                alarmRating.setCompareLastCount(getChangeRate( alarmRating.getAlarmEventCount(),alarmRating.getLastAlarmEventCount()));
                alarmCountingHashMap.put(dateStr, alarmRating);
                alarmRatingResultVO.setAlarmRaingMap(alarmCountingHashMap);
            }
        }
        return alarmRatingResultVO;
    }



    public static AlarmRatingVO monthlyEventCountRate(ElasticsearchClient client, Map<String, Object> queryParameters) {

        AlarmRatingVO alarmRatingResultVO = new AlarmRatingVO();


        String queryDate = queryParameters.get("queryDate").toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(queryDate, formatter);
        int queryYear = date.getYear();
        String startDate = queryYear + "-01-01T00:00:00";
        String endDate = queryYear + "-12-31T23:59:59";
        queryParameters.put("startDate",startDate);
        queryParameters.put("endDate",endDate);
        alarmRatingResultVO.setStartDate(startDate);
        alarmRatingResultVO.setEndDate(endDate);
        HashMap<String, Long> eventCountMap =   queryMonthlyEventCount(client,queryParameters);

        int lastYear = queryYear-1;
        String lastStartDate = lastYear + "-01-01T00:00:00";
        String lastEndDate = lastYear + "-12-31T23:59:59";
        queryParameters.put("startDate",lastStartDate);
        queryParameters.put("endDate", lastEndDate);
        HashMap<String, Long> lastEventCountMap =   queryMonthlyEventCount(client,queryParameters);

        Map<String, AlarmRating>  alarmCountingHashMap = new HashMap<>();

        if(eventCountMap.isEmpty() && !lastEventCountMap.isEmpty()){
            lastEventCountMap.forEach((key, value) -> {

                AlarmRating alarmRating = new AlarmRating();
                alarmRating.setAlarmEventCount(0L);
                alarmRating.setLastAlarmEventCount(value);
                alarmRating.setCompareLastCount(getChangeRate(alarmRating.getAlarmEventCount(),alarmRating.getLastAlarmEventCount()));
                alarmCountingHashMap.put(key, alarmRating);

                alarmRatingResultVO.setAlarmRaingMap(alarmCountingHashMap);
            });

            return alarmRatingResultVO;
        }

        if(!eventCountMap.isEmpty() && lastEventCountMap.isEmpty()){
            eventCountMap.forEach((key, value) -> {

                AlarmRating alarmRating = new AlarmRating();
                alarmRating.setAlarmEventCount(value);
                alarmRating.setLastAlarmEventCount(0L);
                alarmRating.setCompareLastCount(getChangeRate(alarmRating.getAlarmEventCount(),alarmRating.getLastAlarmEventCount()));
                alarmCountingHashMap.put(key, alarmRating);
                alarmRatingResultVO.setAlarmRaingMap(alarmCountingHashMap);
            });
            return alarmRatingResultVO;
        }

        for (String dateStr : eventCountMap.keySet()) {
            if (lastEventCountMap.containsKey(dateStr)) {


                AlarmRating alarmRating = new AlarmRating();

                Long currentMonthCount = eventCountMap.get(dateStr);
                Long lastMonthCount = lastEventCountMap.get(dateStr);

                alarmRating.setAlarmEventCount(currentMonthCount);
                alarmRating.setLastAlarmEventCount(lastMonthCount);
                alarmRating.setCompareLastCount(getChangeRate(alarmRating.getAlarmEventCount(),alarmRating.getLastAlarmEventCount()));
                alarmCountingHashMap.put(dateStr, alarmRating);
                alarmRatingResultVO.setAlarmRaingMap(alarmCountingHashMap);
            }
        }

        return  alarmRatingResultVO;
    }



    public static HashMap<String, Long> queryDailyEventCount(ElasticsearchClient client, Map<String, Object> queryParameters) {
        HashMap<String, Long> eventCountMap = new HashMap<>();
        try {

            BoolQuery.Builder boolQuery = new BoolQuery.Builder();

            if (queryParameters.containsKey("tenantId")) {
                boolQuery.filter(f -> f.term(t -> t.field("tenantId").value(queryParameters.get("tenantId").toString())));
            }

            if (queryParameters.containsKey("deviceChannelId")) {
                boolQuery.filter(f -> f.term(t -> t.field("datas.source.deviceChannelId").value(queryParameters.get("deviceChannelId").toString())));
            }

            if (queryParameters.containsKey("startDate") || queryParameters.containsKey("endDate")) {
                boolQuery.filter(f -> f.range(r -> {
                    r.field("time");
                    if (queryParameters.containsKey("startDate")) {
                        r.gte(JsonData.of(queryParameters.get("startDate").toString()));
                    }
                    if (queryParameters.containsKey("endDate")) {
                        r.lte(JsonData.of(queryParameters.get("endDate").toString()));
                    }
                    return r;
                }));
            }

            // Build the SearchRequest with aggregation
            SearchRequest searchRequest = SearchRequest.of(s -> s
                    .index("*")
                    .size(0)
                    .query(q -> q.bool(boolQuery.build()))
                    .aggregations("daily_event_count", a -> a.dateHistogram(h -> h
                            .field("time")
                            .calendarInterval(CalendarInterval.Day)
                            .format("yyyy-MM-dd")
                            .minDocCount(0)
                            .order(o -> o.key(SortOrder.Asc))))
            );

            // Execute the search
            SearchResponse<Void> searchResponse = client.search(searchRequest, Void.class);

            searchResponse.aggregations()
                    .get("daily_event_count")
                    .dateHistogram()
                    .buckets()
                    .array()
                    .forEach(bucket -> {
                        String date = bucket.keyAsString();
                        Long eventCount = bucket.docCount();
                        eventCountMap.put(date, eventCount);
                    });

        } catch (IOException e) {
            throw new RuntimeException("Failed to execute daily count search request", e);
        }

        return eventCountMap;
    }


    public static HashMap<String, Long> queryMonthlyEventCount(ElasticsearchClient client, Map<String, Object> queryParameters) {
        HashMap<String, Long> monthlyEventCounts = new HashMap<>();
        try {
            BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

            if (queryParameters.containsKey("tenantId")) {
                boolQueryBuilder.filter(f -> f.term(t -> t.field("tenantId").value(queryParameters.get("tenantId").toString())));
            }

            if (queryParameters.containsKey("deviceChannelId")) {
                boolQueryBuilder.filter(f -> f.term(t -> t.field("datas.source.deviceChannelId").value(queryParameters.get("deviceChannelId").toString())));
            }

            String startDate = queryParameters.get("startDate").toString();
            String endDate = queryParameters.get("endDate").toString();
            boolQueryBuilder.must(Query.of(q -> q.range(RangeQuery.of(r -> r.field("time").gte(JsonData.of(startDate)).lte(JsonData.of(endDate))))));

            Query query = Query.of(q -> q.bool(boolQueryBuilder.build()));

            // Create a monthly date histogram aggregation
            DateHistogramAggregation aggregation = DateHistogramAggregation.of(b -> b
                    .field("time")
                    .calendarInterval(CalendarInterval.Month) // Group by month
                    .format("yyyy-MM")
                    .minDocCount(0)
                    .order(o -> o.key(SortOrder.Asc)));

            SearchRequest searchRequest = SearchRequest.of(s -> s
                    .index("*")
                    .size(0) // No need to return documents, just the aggregation
                    .query(query)
                    .aggregations("monthly_event_count", a -> a.dateHistogram(aggregation)));

            SearchResponse<Void> searchResponse = client.search(searchRequest, Void.class);

            searchResponse.aggregations().get("monthly_event_count").dateHistogram().buckets().array().forEach(bucket -> {
                String formattedMonth = bucket.keyAsString();
                Long count = bucket.docCount();
                monthlyEventCounts.put(formattedMonth, count);
            });


        } catch (IOException e) {
            throw new RuntimeException("Failed to execute monthly count search request", e);
        }
        return monthlyEventCounts;
    }





    public static double getChangeRate(Long currentCount, Long lastCount) {
        return currentCount != 0 && lastCount == 0 ? currentCount * 100 :
                currentCount == 0 && lastCount != 0 ? -(lastCount * 100) :
                        lastCount != 0 ? ((double) (currentCount - lastCount) / lastCount) * 100 : 0.0;
    }

}
