package com.macro.study.test.rabbitmq.scenario02_WorkQueues;

import com.macro.study.test.rabbitmq.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

public class Workder {

    public static void main(String[] args) throws Exception {

        Connection connection = RabbitMQUtil.getConnection() ;
        Channel channel = connection.createChannel();


        channel.queueDeclare(RabbitMQUtil.QUEUE_NAME, true, false, false, null);

        channel.basicQos(1);//每个worker同时最多只处理一个消息
    //    channel.queueBind(RabbitMQUtil.QUEUE_NAME, RabbitMQUtil.EXCHANGE_NAME, RabbitMQUtil.ROUTINGKEY);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");

            System.out.println(" [x] Received '" + message + "'");
            try {
                doWork(message);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                System.out.println(" [x] Done");
            }
        };
        boolean autoAck = true; // acknowledgment is covered below
        channel.basicConsume(RabbitMQUtil.QUEUE_NAME, autoAck, deliverCallback, consumerTag -> { });



    }

    private static void doWork(String task) throws InterruptedException {
        for (char ch: task.toCharArray()) {
            if (ch == '.') Thread.sleep(1000);
        }
    }
}
