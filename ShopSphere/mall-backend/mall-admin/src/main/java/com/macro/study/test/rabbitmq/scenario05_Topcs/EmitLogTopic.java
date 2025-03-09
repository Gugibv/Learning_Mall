package com.macro.study.test.rabbitmq.scenario05_Topcs;

import com.macro.study.test.rabbitmq.RabbitMQUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class EmitLogTopic {
    public static final String EXCHANGE_NAME="myExchange";

    public static void main(String []args) throws Exception {
      ;
        try ( Connection connection = RabbitMQUtil.getConnection();
              Channel channel = connection.createChannel();
             ){
            //发送者只管往exchange里发消息，而不用关心具体发到哪些queue里。
            channel.exchangeDeclare(EXCHANGE_NAME,"topic");
            String message = "LOG INFO";
            channel.basicPublish(EXCHANGE_NAME, "anonymous.info", null, message.getBytes());
            channel.basicPublish(EXCHANGE_NAME, "grey.wow.debug", null, message.getBytes());


        };
    }

}
