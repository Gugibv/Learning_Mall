package com.macro.study.test.rabbitmq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQUtil {

    private static Connection connection;
    public static final String HOST_NAME="127.0.0.1";
    public static final int HOST_PORT=5672;

    public static final String USER_NAME="admin";
    public static final String PASSWORD="admin";
    public static final String VIRTUAL_HOST="grey";

    public static final String QUEUE_NAME="test";
    public static final String EXCHANGE_NAME="myExchange";
    public static final String ROUTINGKEY="IamGrey";

    private RabbitMQUtil() {}

    public static Connection getConnection() throws Exception {
        if(null == connection) {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(HOST_NAME);
            factory.setPort(HOST_PORT);
            factory.setUsername(USER_NAME);
            factory.setPassword(PASSWORD);
            factory.setVirtualHost(VIRTUAL_HOST);
            connection = factory.newConnection();
        }
        return connection;
    }
}
