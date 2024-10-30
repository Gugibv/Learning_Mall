package com.grey.elasticsearch.test.utils;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.CalendarInterval;
import co.elastic.clients.elasticsearch._types.aggregations.DateHistogramAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import com.grey.elasticsearch.test.bean.AlarmCountingVO;
import com.grey.elasticsearch.test.bean.QueryParametersDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import java.io.IOException;
import java.util.*;


@Slf4j
public class AlertTrendAnalysisApi {

    public static void queryAllEventData(ElasticsearchClient client, QueryParametersDTO queryParametersDTO) {
        List<String> deviceChannelId = queryParametersDTO.getDeviceChannelId();
        deviceChannelId.forEach(channeid ->{

            try {
                // Create date range query
                Query dateRangeQuery = RangeQuery.of(r -> r
                        .field("time")
                        .gte(JsonData.of(queryParametersDTO.getQueryDate()))
                        .lte(JsonData.of(queryParametersDTO.getQueryDate()))
                )._toQuery();

                // Check if tenantId is present and add it to the query
                Query tenantIdQuery = null;
                if (!StringUtils.isEmpty(queryParametersDTO.getTenantId())) {
                    tenantIdQuery = MatchQuery.of(m -> m
                            .field("tenantId")
                            .query(queryParametersDTO.getTenantId())
                    )._toQuery();
                }

                // Check if deviceChannelId is present and add it to the query
                Query deviceChannelIdQuery = null;

                deviceChannelIdQuery = MatchQuery.of(m -> m
                        .field("datas.source.deviceChannelId")
                        .query(channeid)
                )._toQuery();


                // Build the final query
                BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder().must(dateRangeQuery);
                if (tenantIdQuery != null) {
                    boolQueryBuilder.must(tenantIdQuery);
                }
                if (deviceChannelIdQuery != null) {
                    boolQueryBuilder.must(deviceChannelIdQuery);
                }

                Query finalQuery = boolQueryBuilder.build()._toQuery();

                // Build the search request
                SearchRequest searchRequest = SearchRequest.of(s -> s
                        .index("*") // Query all indices that match the pattern
                        .size(1000)  // Limit the result size
                        .query(finalQuery));

                // Execute the search
                SearchResponse<Object> searchResponse = client.search(searchRequest, Object.class);

                // Print the search results
                searchResponse.hits().hits().forEach(hit -> {
                    System.out.println(hit.source());
                });
            } catch (IOException e) {
                throw new RuntimeException("Failed to execute search request", e);
            }

        });
    }


    public static List<AlarmCountingVO> queryHourlyEventCount(ElasticsearchClient client, QueryParametersDTO queryParametersDTO) {

        List<String> deviceChannelId = queryParametersDTO.getDeviceChannelId();
        List<AlarmCountingVO> alarmCountingVOList = new ArrayList<>();

        deviceChannelId.forEach(channeid ->{
            List<Map<String,Long>> countingList = new ArrayList<>();
            AlarmCountingVO alarmCountingVO = new AlarmCountingVO();

            alarmCountingVO.setDeviceChannelId(channeid);

            Map<String, Long> hourlyEventCounts = new HashMap<>();

            String queryDate = queryParametersDTO.getQueryDate().toString();

            try {
                BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

                if (!StringUtils.isEmpty(queryParametersDTO.getTenantId())) {
                    boolQueryBuilder.filter(f -> f.term(t -> t.field("tenantId").value(queryParametersDTO.getTenantId())));
                }

                boolQueryBuilder.filter(f -> f.term(t -> t.field("datas.source.deviceChannelId").value(channeid)));

                String dateStart = queryDate + "T00:00:00";
                String dateEnd = queryDate + "T23:59:59";
                boolQueryBuilder.must(Query.of(q -> q.range(RangeQuery.of(r -> r.field("time").gte(JsonData.of(dateStart)).lte(JsonData.of(dateEnd))))));

                Query query = Query.of(q -> q.bool(boolQueryBuilder.build()));

                // Create an hourly date histogram aggregation
                DateHistogramAggregation aggregation = DateHistogramAggregation.of(b -> b
                        .field("time")
                        .calendarInterval(CalendarInterval.Hour) // Group by hour
                        .format("yyyy-MM-dd HH")
                        .minDocCount(0));

                SearchRequest searchRequest = SearchRequest.of(s -> s
                        .index("*")
                        .size(0) // No need to return documents, just the aggregation
                        .query(query)
                        .aggregations("hourly_event_count", a -> a.dateHistogram(aggregation)));

                SearchResponse<Void> searchResponse = client.search(searchRequest, Void.class);

                searchResponse.aggregations().get("hourly_event_count").dateHistogram().buckets().array().forEach(bucket -> {
                    long timestamp = bucket.key().toEpochMilli();
                    String formattedHour = DateFormatUtils.format(new Date(timestamp), "yyyy-MM-dd HH");
                    long count = bucket.docCount();
                    hourlyEventCounts.put(formattedHour, count);
                });


            } catch (IOException e) {
                throw new RuntimeException("Failed to execute hourly count search request", e);
            }
            countingList.add( hourlyEventCounts);
            alarmCountingVO.setCountingList(countingList);
            alarmCountingVOList.add(alarmCountingVO);
        });


        return  alarmCountingVOList;
    }

    public static  List<AlarmCountingVO>  queryDailyEventCount(ElasticsearchClient client,  QueryParametersDTO queryParametersDTO) {

        List<String> deviceChannelId = queryParametersDTO.getDeviceChannelId();
        List<AlarmCountingVO> alarmCountingVOList = new ArrayList<>();



        deviceChannelId.forEach(channeid ->{
            List<Map<String,Long>> countingList = new ArrayList<>();
            AlarmCountingVO alarmCountingVO = new AlarmCountingVO();

            alarmCountingVO.setDeviceChannelId(channeid);
            alarmCountingVO.setStartDate(queryParametersDTO.getStartDate());
            alarmCountingVO.setEndDate(queryParametersDTO.getEndDate());
            HashMap<String, Long> DailyCountingHashMap = new HashMap<>();

            try {
                BoolQuery.Builder boolQuery = new BoolQuery.Builder();

                if (!StringUtils.isEmpty(queryParametersDTO.getTenantId())) {
                    boolQuery.filter(f -> f.term(t -> t.field("tenantId").value(queryParametersDTO.getTenantId())));
                }

                boolQuery.filter(f -> f.term(t -> t.field("datas.source.deviceChannelId").value(channeid)));


                boolQuery.filter(f -> f.range(r -> {
                    r.field("time");
                    r.gte(JsonData.of(queryParametersDTO.getStartDate()));
                    r.lte(JsonData.of(queryParametersDTO.getEndDate()));
                    return r;
                }));


                // Build the SearchRequest with aggregation
                SearchRequest searchRequest = SearchRequest.of(s -> s
                        .index("*")
                        .size(0)
                        .query(q -> q.bool(boolQuery.build()))
                        .aggregations("daily_event_count", a -> a.dateHistogram(h -> h
                                .field("time")
                                .calendarInterval(CalendarInterval.Day)
                                .format("yyyy-MM-dd")
                                .minDocCount(0)))
                );

                // Execute the search
                SearchResponse<Void> searchResponse = client.search(searchRequest, Void.class);

                // Process and print the results
                searchResponse.aggregations()
                        .get("daily_event_count")
                        .dateHistogram()
                        .buckets()
                        .array()
                        .forEach(bucket -> {
                            String date = bucket.keyAsString();
                            Long eventCount =  bucket.docCount();

                            DailyCountingHashMap.put(date, eventCount);
                        });

            } catch (IOException e) {
                throw new RuntimeException("Failed to execute daily count search request", e);
            }

            countingList.add(DailyCountingHashMap);
            alarmCountingVO.setCountingList(countingList);
            alarmCountingVOList.add(alarmCountingVO);
        });


        return  alarmCountingVOList;
    }
}
