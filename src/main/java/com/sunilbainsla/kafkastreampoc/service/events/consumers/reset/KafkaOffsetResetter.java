package com.sunilbainsla.kafkastreampoc.service.events.consumers.reset;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.*;

import java.time.Duration;
import java.util.*;

public class KafkaOffsetResetter {

    public static void main(String[] args) {
        String topic = "topic3";
        int partition = 0;
        long desiredOffset = 10; // Specify the desired offset value

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "kafka-stream-poc-topic3Consumer");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");


        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        TopicPartition topicPartition = new TopicPartition(topic, partition);

        consumer.assign(Arrays.asList(topicPartition));
        consumer.seek(topicPartition, desiredOffset);


        try {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                // Process the replayed message
                System.out.println("Replayed message: " + record.value());
            }
        } catch (KafkaException e) {
            // Handle any Kafka exception
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }


}
