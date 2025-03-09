package com.macro.study.test.rabbitmq.scenario05_Topcs;

import com.macro.study.test.rabbitmq.RabbitMQUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

public class ReceiveLogsTopic {
    public static final String EXCHANGE_NAME="myExchange";
    public static void main(String []args) throws Exception {
        try(Connection connection = RabbitMQUtil.getConnection();
            Channel channel = connection.createChannel();
         ){
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            String queueName = channel.queueDeclare().getQueue();
            //topic的routingkey，*代表一个具体的单词，#代表0个或多个单词。
            channel.queueBind(queueName, EXCHANGE_NAME, "*.info");
            channel.queueBind(queueName, EXCHANGE_NAME, "#.debug");

            Consumer myconsumer = new DefaultConsumer(channel) {
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           BasicProperties properties, byte[] body)
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
                    //消息处理完后，进行答复。答复过的消息，服务器就不会再次转发。
                    //没有答复过的消息，服务器会一直不停转发。
//				 channel.basicAck(deliveryTag, false);
                }
            };
            channel.basicConsume(queueName,true, myconsumer);
        };


    }

}
