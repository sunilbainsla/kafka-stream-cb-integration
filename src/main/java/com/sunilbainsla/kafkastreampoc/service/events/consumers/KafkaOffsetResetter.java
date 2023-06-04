package com.sunilbainsla.kafkastreampoc.service.events.consumers;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.*;
import java.util.*;

public class KafkaOffsetResetter {

    public static void main(String[] args) {
        String topic = "topic2";
        int partition = 0;
        long desiredOffset = 22; // Specify the desired offset value

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "kafka-stream-poc-topic2Consumer");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");


        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        TopicPartition topicPartition = new TopicPartition(topic, partition);

        consumer.assign(Arrays.asList(topicPartition));
        consumer.seek(topicPartition, desiredOffset);

        consumer.close();
    }


}
