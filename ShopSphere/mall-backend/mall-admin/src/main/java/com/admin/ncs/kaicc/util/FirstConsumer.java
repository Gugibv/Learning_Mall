package com.admin.ncs.kaicc.util;

import com.rabbitmq.client.*;

import java.io.IOException;

public class FirstConsumer {

    private static final String HOST_NAME="172.31.2.200";
    private static final int HOST_PORT=5672;
    private static final String QUEUE_NAME="test";
    public static final String USER_NAME="vade";
    public static final String PASSWORD="vade123456";
    public static final String VIRTUAL_HOST="/grey";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST_NAME);
        factory.setPort(HOST_PORT);
        factory.setUsername(USER_NAME);
        factory.setPassword(PASSWORD);
        factory.setVirtualHost(VIRTUAL_HOST);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        /**
         * 声明一个对列。几个参数依次为： 队列名，durable是否实例化；exclusive：是否独占；
         autoDelete：是否自动删除；arguments:参数
         * 这几个参数跟创建队列的页面是一致的。
         * 如果Broker上没有队列，那么就会自动创建队列。
         * 但是如果Broker上已经由了这个队列。那么队列的属性必须匹配，否则会报错。
         */

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        //每个worker同时最多只处理一个消息
        channel.basicQos(1);

        //回调函数，处理接收到的消息
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
        channel.basicConsume(QUEUE_NAME, myconsumer);

    }

}
