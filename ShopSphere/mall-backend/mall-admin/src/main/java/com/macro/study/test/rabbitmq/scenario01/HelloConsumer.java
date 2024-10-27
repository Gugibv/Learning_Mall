package com.macro.study.test.rabbitmq.scenario01;

import com.macro.study.test.rabbitmq.RabbitMQUtil;
import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;

public class HelloConsumer {

    public static void main(String[] args) throws Exception {

        Connection connection = RabbitMQUtil.getConnection() ;
        Channel channel = connection.createChannel();


        channel.queueDeclare(RabbitMQUtil.QUEUE_NAME, true, false, false, null);

        channel.basicQos(1);//每个worker同时最多只处理一个消息
        channel.queueBind(RabbitMQUtil.QUEUE_NAME, RabbitMQUtil.EXCHANGE_NAME, RabbitMQUtil.ROUTINGKEY);



/*
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
*/

        // 设置手动确认，但不执行 basicAck()
        channel.basicConsume(RabbitMQUtil.QUEUE_NAME, false, (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received but not acknowledged: " + message);
            // **不调用 channel.basicAck()，让消息留在 Unacked 状态**
        }, consumerTag -> {});

        System.out.println(" [*] Waiting for messages...");

/*        //回调函数，处理接收到的消息
        Consumer myconsumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                System.out.println("========================");
                String routingKey = envelope.getRoutingKey();
                System.out.println("routingKey >"+routingKey);
                String contentType = properties.getContentType();
                System.out.println("contentType >"+contentType);
                long deliveryTag = envelope.getDeliveryTag();
                System.out.println("deliveryTag >"+deliveryTag);
                System.out.println("content:"+new String(body,"UTF-8"));
                // (process the message components here ...)
                channel.basicAck(deliveryTag, false);
            }
        };

        //从test1队列接收消息
        channel.basicConsume(QUEUE_NAME, myconsumer);*/

    }

}
