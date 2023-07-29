package com.sunilbainsla.kafkastreampoc.service.events.consumers.reset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.AlterConsumerGroupOffsetsResult;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.internals.ConsumerCoordinator;
import org.apache.kafka.clients.consumer.internals.ConsumerMetadata;
import org.apache.kafka.common.TopicPartition;

public class KafkaOffsetResetExample {

    public static void main(String[] args) {
        // Set the properties for the AdminClient
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka-stream-poc-topic3Consumer");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        // Create the consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        // Pause the consumer to stop fetching records

        // Create the AdminClient
        try (AdminClient adminClient = AdminClient.create(props)) {
            // Specify the consumer group, topic, partition, and offset
            String consumerGroup = "kafka-stream-poc-topic3Consumer";
            String topic = "topic3";
            int partition = 0;
            long offset = 1;

            // Create the TopicPartition object
            TopicPartition topicPartition = new TopicPartition(topic, partition);

            // Create the OffsetAndMetadata object
            OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(offset);

            // Create the map of topic partitions and offset metadata
            Map<TopicPartition, OffsetAndMetadata> offsetsMap = new HashMap<>();
            offsetsMap.put(topicPartition, offsetAndMetadata);
            consumer.pause(Collections.emptyList());
            consumer.unsubscribe();
            stopConsumerGracefully(consumer);
            // Reset the offsets for the specified consumer group
            AlterConsumerGroupOffsetsResult result = adminClient.alterConsumerGroupOffsets(consumerGroup, offsetsMap);
            result.all().get();

            consumer.commitSync();

            // Resume the consumer to start fetching records again
            consumer.resume(Collections.emptyList());
            System.out.println("Offset reset successful");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void stopConsumerGracefully(KafkaConsumer<String, String> consumer) {
        AtomicBoolean stopFlag = new AtomicBoolean(false);

        // Register a shutdown hook to capture termination signals
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Set the stop flag when the shutdown hook is triggered
            stopFlag.set(true);
            // Interrupt the consumer thread to break out of the polling loop
           // consumer.wakeup();
        }));

    }
}
