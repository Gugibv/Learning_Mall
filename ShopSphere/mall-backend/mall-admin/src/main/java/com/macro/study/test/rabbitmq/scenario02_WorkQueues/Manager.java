package com.macro.study.test.rabbitmq.scenario02_WorkQueues;

import com.macro.study.test.rabbitmq.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Manager {
     public static void main(String[] args) throws Exception{

        // Note we can use a try-with-resources statement because both Connection and Channel implement java.lang.AutoCloseable.
        // This way we don't need to close them explicitly in our code.
        try (Connection connection = RabbitMQUtil.getConnection() ;
             Channel channel = connection.createChannel();
             Scanner scanner = new Scanner(System.in)) {

        //    channel.exchangeDeclare(RabbitMQUtil.EXCHANGE_NAME, "direct", false, false, null);
            channel.queueDeclare(RabbitMQUtil.QUEUE_NAME, true, false, false, null);
        //    channel.queueBind(RabbitMQUtil.QUEUE_NAME, RabbitMQUtil.EXCHANGE_NAME, RabbitMQUtil.ROUTINGKEY);

            System.out.println("Enter messages to send (type 'exit' to quit):");

            while (true) {
                System.out.print("> ");
                String message = scanner.nextLine();

                if ("exit".equalsIgnoreCase(message)) {
                    System.out.println("Exiting...");
                    break; // 退出循环
                }

           //     channel.basicPublish(RabbitMQUtil.EXCHANGE_NAME, RabbitMQUtil.ROUTINGKEY, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));

                channel.basicPublish("", RabbitMQUtil.QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));


                System.out.println(" [x] Sent: '" + message + "'");
            }
        }
    }
}
