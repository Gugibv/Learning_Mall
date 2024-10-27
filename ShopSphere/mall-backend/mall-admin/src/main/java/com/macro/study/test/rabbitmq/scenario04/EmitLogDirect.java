package com.macro.study.test.rabbitmq.scenario04;

import com.macro.study.test.rabbitmq.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class EmitLogDirect {
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] argv) throws Exception {

        try (Connection connection = RabbitMQUtil.getConnection();
             Channel channel = connection.createChannel()) {

            // 声明 Direct 交换机
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");


            // 获取日志级别（routing key）
            String severity = "Error";
            // 获取日志内容
            String message = getMessage(severity);

            // 发送消息
            channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + severity + "':'" + message + "'");
        }
    }


    private static String getMessage(String severity) {
        return String.join(" ", severity , "This is an error log from docker...");
    }
}
