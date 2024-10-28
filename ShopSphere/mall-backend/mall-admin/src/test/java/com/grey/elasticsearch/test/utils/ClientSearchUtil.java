package com.grey.elasticsearch.test.utils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.CalendarInterval;
import co.elastic.clients.elasticsearch._types.aggregations.DateHistogramAggregation;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.grey.elasticsearch.test.bean.VaEvent;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientSearchUtil {


    public static ElasticsearchClient createLocalElasticsearchClient() {
        RestClientBuilder builder = RestClient.builder(new HttpHost("localhost", 9200, "http"));
        RestClient restClient = builder.build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new co.elastic.clients.json.jackson.JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }

    public static void closeClient(ElasticsearchClient client) {
        try {
            client._transport().close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to close Elasticsearch client", e);
        }
    }

    public static void queryAllEventData(ElasticsearchClient client) {
        try {
            SearchRequest searchRequest = SearchRequest.of(s -> s
                    .index("events-*") // Query all indices that match the pattern
                    .size(1000)); // Limit the result size

            SearchResponse<VaEvent> searchResponse = client.search(searchRequest, VaEvent.class);
            List<Hit<VaEvent>> hits = searchResponse.hits().hits();

            System.out.println("Total events found: " + hits.size());
            for (Hit<VaEvent> hit : hits) {
                System.out.println(hit.source());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to execute search request", e);
        }
    }


    public static void queryDailyEventCount(ElasticsearchClient client) {
        try {
            DateHistogramAggregation aggregation = DateHistogramAggregation.of(b -> b
                    .field("time")
                    .calendarInterval(CalendarInterval.Day) // Group by day
                    .format("yyyy-MM-dd")
                    .minDocCount(0));

            SearchRequest searchRequest = SearchRequest.of(s -> s
                    .index("events-*")
                    .size(0) // No need to return documents, just the aggregation
                    .aggregations("daily_event_count", a -> a.dateHistogram(aggregation)));

            SearchResponse<Void> searchResponse = client.search(searchRequest, Void.class);
            Map<String, Long> dailyEventCounts = new HashMap<>();
            searchResponse.aggregations().get("daily_event_count").dateHistogram().buckets().array().forEach(bucket -> {
                long timestamp = bucket.key();
                String formattedDate = DateFormatUtils.format(new Date(timestamp), "yyyy-MM-dd");
                long count = bucket.docCount();
                dailyEventCounts.put(formattedDate, count);
            });

            dailyEventCounts.forEach((day, count) -> {
                System.out.println("Date: " + day + " , Event Count: " + count);
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to execute daily count search request", e);
        }
    }


}
