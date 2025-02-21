package com.admin.ncs.kaicc.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class FirstProducer {
    private static final String HOST_NAME="172.31.2.200";
    private static final int HOST_PORT=5672;
    private static final String QUEUE_NAME="test";
    public static final String USER_NAME="vade";
    public static final String PASSWORD="vade123456";
    public static final String VIRTUAL_HOST="/grey";

    public static void main(String[] args) throws Exception{
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
         你看，整个流程跟消费者是不是差不多的？除了生产者发送完消息后需要主动关闭下连接，而消费者因为
         要持续消费消息所以不需要主动关闭连接，其他流程几乎完全一样的。
         * 如果Broker上没有队列，那么就会自动创建队列。
         * 但是如果Broker上已经由了这个队列。那么队列的属性必须匹配，否则会报错。
         */
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        String message = "message";
        channel.basicPublish("", QUEUE_NAME,
                MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        channel.close();
        connection.close();

    }
}
