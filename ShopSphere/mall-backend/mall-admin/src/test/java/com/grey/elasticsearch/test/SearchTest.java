package com.grey.elasticsearch.test;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.grey.elasticsearch.test.utils.ClientSearchUtil;

public class SearchTest {

    public static void main(String []args){
        ElasticsearchClient client = ClientSearchUtil.createLocalElasticsearchClient();
     //   EventGenerator.generateAndStoreEventData(client);
     //   ElasticsearchClientUtil.queryAllEventData(client);
        ClientSearchUtil.queryDailyEventCount(client);
        ClientSearchUtil.closeClient(client);
    }

}
