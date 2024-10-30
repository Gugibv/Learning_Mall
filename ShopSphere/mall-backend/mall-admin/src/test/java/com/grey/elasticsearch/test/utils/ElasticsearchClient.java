package com.grey.elasticsearch.test.utils;

import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import java.io.IOException;

public class ElasticsearchClient {

    public static co.elastic.clients.elasticsearch.ElasticsearchClient createLocalElasticsearchClient() {
        String username= "xxx";
        String password = "xxx";
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

        RestClientBuilder builder = RestClient.builder(new HttpHost("127.0.0.1", 9200, "http"))
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));

        RestClient restClient = builder.build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new co.elastic.clients.json.jackson.JacksonJsonpMapper());

        return new co.elastic.clients.elasticsearch.ElasticsearchClient(transport);
    }

    public static co.elastic.clients.elasticsearch.ElasticsearchClient createLocalElasticsearchClientNoPWD() {
        RestClientBuilder builder = RestClient.builder(new HttpHost("localhost", 9200, "http"));
        RestClient restClient = builder.build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new co.elastic.clients.json.jackson.JacksonJsonpMapper());
        return new co.elastic.clients.elasticsearch.ElasticsearchClient(transport);
    }


    public static void closeClient(co.elastic.clients.elasticsearch.ElasticsearchClient client) {
        try {
            client._transport().close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to close Elasticsearch client", e);
        }
    }

}
