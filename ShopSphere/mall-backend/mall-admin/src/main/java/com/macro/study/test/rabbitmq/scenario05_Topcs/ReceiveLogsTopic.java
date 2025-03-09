package com.macro.study.test.rabbitmq.scenario05_Topcs;

import com.macro.study.test.rabbitmq.RabbitMQUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ReceiveLogsTopic {
    public static final String EXCHANGE_NAME="topicExchange";

    public static void main(String []args) throws Exception {

            Connection connection = RabbitMQUtil.getConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, "topic");

            String queueName = channel.queueDeclare().getQueue();

            //topic的 routingkey，*代表一个具体的单词，#代表0个或多个单词。
            channel.queueBind(queueName, EXCHANGE_NAME, "*.info");
            channel.queueBind(queueName, EXCHANGE_NAME, "#.debug");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [x] Received '" +
                        delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });



    }

}
