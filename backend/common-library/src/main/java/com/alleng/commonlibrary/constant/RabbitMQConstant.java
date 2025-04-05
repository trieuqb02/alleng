package com.alleng.commonlibrary.constant;

public class RabbitMQConstant {
    // exchange
    public static final String EXCHANGE_TOPIC_NEWS = "news.exchange.topic";
    public static final String EXCHANGE_DIRECT_NEWS = "news.exchange.direct";

    // routing key
    public static final String ROUTING_KEY_TOPIC_FILE = "file.*";
    public static final String ROUTING_KEY_TOPIC_FILE_CREATE = "file.create";
    public static final String ROUTING_KEY_TOPIC_FILE_DELETE = "file.delete";
    public static final String ROUTING_KEY_TOPIC_FILE_UPDATE = "file.update";

    public static final String ROUTING_KEY_DIRECT_FILE_COMPLETED = "news.file.completed";
    public static final String ROUTING_KEY_DIRECT_FILE_FAILED = "news.file.failed";

    // queue
    public static final String QUEUE_FILE = "file";
    public static final String QUEUE_NEWS_COMPLETED = "news.file.completed";
    public static final String QUEUE_NEWS_FAILED = "news.file.failed";
}
