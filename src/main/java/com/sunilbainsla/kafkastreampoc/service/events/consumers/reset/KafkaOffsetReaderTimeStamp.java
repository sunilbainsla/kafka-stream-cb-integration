package com.sunilbainsla.kafkastreampoc.service.events.consumers;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.*;
import java.time.*;
import java.util.*;

public class KafkaOffsetReaderTimeStamp {
    public static void main(String[] args) {
        String topic = "topic3";
        int partition = 0;
       Instant desiredTimestamp = Instant.parse("2023-06-01T12:00:00Z");
//        long desiredTimestampMillis = 1686043843674L;
//        //Instant desiredTimestamp = Instant.ofEpochMilli(desiredTimestampMillis);

        KafkaOffsetReaderTimeStamp messageReader = new KafkaOffsetReaderTimeStamp();
        messageReader.readMessageFromTimestamp(topic, partition, desiredTimestamp);

    }
    public void readMessageFromTimestamp(String topic, int partition, Instant timestamp) {
        Properties props = new Properties();
        props.put("enable.auto.commit", "false");
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "kafka-stream-poc-topic3Consumer");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        TopicPartition topicPartition = new TopicPartition(topic, partition);

        consumer.assign(Arrays.asList(topicPartition));
        consumer.seekToBeginning(Collections.singleton(topicPartition));

        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

        long desiredOffset = searchForOffsetByTimestamp(records, topicPartition, timestamp);
        if (desiredOffset >= 0) {
            consumer.seek(topicPartition, desiredOffset);

            ConsumerRecords<String, String> recordss = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : recordss) {
                System.out.println("Offset: " + record.offset() + ", My Value+>>>>>>: " + record.value());
            }
        } else {
            System.out.println("No message found for the specified timestamp.");
        }

        consumer.close();
    }

    private long searchForOffsetByTimestamp(ConsumerRecords<String, String> records, TopicPartition topicPartition, Instant timestamp) {
        for (ConsumerRecord<String, String> record : records.records(topicPartition)) {
            if (record.timestamp() >= timestamp.toEpochMilli()) {
                return record.offset();
            }
        }
        return -1;
    }
}
