package com.sunilbainsla.kafkastreampoc.service.events.consumers;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.*;

import java.time.Duration;
import java.util.*;

public class KafkaOffsetReader {

    public static void main(String[] args) {
        String topic = "topic3";
        int partition = 0;
        long offset = 9
                ; // Specify the desired offset value

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "kafka-stream-poc-topic3Consumer");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        TopicPartition topicPartition = new TopicPartition(topic, partition);

        consumer.assign(Arrays.asList(topicPartition));
        consumer.seek(topicPartition, offset);

        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

        for (ConsumerRecord<String, String> record : records) {
            System.out.println("Offset: " + record.offset() + ", My Value+>>>>>>: " + record.value());
        }

        consumer.close();
    }
}
