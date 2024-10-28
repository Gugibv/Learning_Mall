package com.grey.elasticsearch.test.utils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import com.grey.elasticsearch.test.bean.VaEvent;
import com.grey.elasticsearch.test.var.MyConst;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.IOException;
import java.util.*;

public class EventGenerator {

    public static void generateAndStoreEventData(ElasticsearchClient client) {
        List<BulkOperation> operations = new ArrayList<>();
        int[] eventCounts = {10, 20, 30, 40, 50};

        for (int day = 1; day <= 5; day++) {
            int count = eventCounts[day - 1];
            long baseTime = 1728000000000L + (day - 1) * 86400000L; // Example base timestamp for each day

            for (int i = 0; i < count; i++) {
                long eventTime = baseTime + i * 1000;
                String indexName = String.format(MyConst.ALERT_INDEX_FORMAT, DateFormatUtils.format(new Date(eventTime), MyConst.DATE_FORMAT, TimeZone.getTimeZone("GMT+8:00")));
                String eventId = "event-" + day + "-" + i;

                // Create an event document
                VaEvent event = new VaEvent();
                event.setId(eventId);
                event.setTime(eventTime);
                event.setType("event.generic.recognition");
                event.setAlertName("simulated_event");
                event.setSeverity("MINOR");

                // Add to bulk operations
                operations.add(BulkOperation.of(op -> op
                        .index(idx -> idx
                                .index(indexName)
                                .id(eventId)
                                .document(event)
                        )));
            }
        }

        BulkRequest bulkRequest = BulkRequest.of(b -> b.operations(operations));
        try {
            BulkResponse response = client.bulk(bulkRequest);
            System.out.println("Bulk insert response: " + response);
        } catch (IOException e) {
            throw new RuntimeException("Failed to execute bulk insert", e);
        }
    }




}
