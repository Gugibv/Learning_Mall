package com.macro.study.test.rabbitmq.scenario04_Routing;

import com.macro.study.test.rabbitmq.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class EmitLogDirect {
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] argv) throws Exception {

        try (Connection connection = RabbitMQUtil.getConnection();
             Channel channel = connection.createChannel()) {

            //发送者只管往exchange里发消息，而不用关心具体发到哪些queue里。
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            String message = "LOG INFO。。。。";

            // 发送消息
            channel.basicPublish(EXCHANGE_NAME, "info", null, message.getBytes("UTF-8"));
            channel.basicPublish(EXCHANGE_NAME, "debug", null, message.getBytes("UTF-8"));
            channel.basicPublish(EXCHANGE_NAME, "warn", null, message.getBytes("UTF-8"));


        }
    }

}
