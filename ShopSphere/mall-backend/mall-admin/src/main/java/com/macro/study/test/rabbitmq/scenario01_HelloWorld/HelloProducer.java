package com.macro.study.test.rabbitmq.scenario01_HelloWorld;

import com.macro.study.test.rabbitmq.RabbitMQUtil;
import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;

public class HelloProducer {

    public static final String EXCHANGE_NAME="myExchange";
    public static void main(String[] args) throws Exception{
        // Note we can use a try-with-resources statement because both Connection and Channel implement java.lang.AutoCloseable.
        // This way we don't need to close them explicitly in our code.
        try (Connection connection = RabbitMQUtil.getConnection() ;
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME,"direct", false,false,null); // 可选
            channel.queueDeclare(RabbitMQUtil.QUEUE_NAME, true, false, false, null);
            channel.queueBind(RabbitMQUtil.QUEUE_NAME,EXCHANGE_NAME, RabbitMQUtil.ROUTINGKEY);

            String message = "message1";
            //channel.basicPublish(EXCHANGE_NAME, QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));
            channel.basicPublish(EXCHANGE_NAME, RabbitMQUtil.ROUTINGKEY, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
