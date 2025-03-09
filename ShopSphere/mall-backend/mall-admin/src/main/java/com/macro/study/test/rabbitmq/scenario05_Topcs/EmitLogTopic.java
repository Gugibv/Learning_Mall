package com.macro.study.test.rabbitmq.scenario05_Topcs;

import com.macro.study.test.rabbitmq.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.nio.charset.StandardCharsets;

public class EmitLogTopic {
    public static final String EXCHANGE_NAME="topicExchange";

    public static void main(String []args) throws Exception {
        try ( Connection connection = RabbitMQUtil.getConnection();
              Channel channel = connection.createChannel();
             ){

            //发送者只管往exchange里发消息，而不用关心具体发到哪些queue里。
            channel.exchangeDeclare(EXCHANGE_NAME,"topic");
            String message1 = "LOG INFO...";
            String message2 = "DEBUG INFO...";
            channel.basicPublish(EXCHANGE_NAME, "anonymous.info", null,  message1.getBytes(StandardCharsets.UTF_8));
            channel.basicPublish(EXCHANGE_NAME, "grey.wow.debug", null, message2.getBytes(StandardCharsets.UTF_8));


        };
    }

}
