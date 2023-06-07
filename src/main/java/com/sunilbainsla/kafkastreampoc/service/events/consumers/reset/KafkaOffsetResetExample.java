package com.sunilbainsla.kafkastreampoc.service.events.consumers.reset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.AlterConsumerGroupOffsetsResult;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

public class KafkaOffsetResetExample {

    public static void main(String[] args) {
        // Set the properties for the AdminClient
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        // Create the AdminClient
        try (AdminClient adminClient = AdminClient.create(props)) {
            // Specify the consumer group, topic, partition, and offset
            String consumerGroup = "kafka-stream-poc-topic3Consumer-e51a9efc-47bf-4e76-99ba-34cdfc070757-StreamThread-1-consumer-93fa214c-1756-4a86-baa3-69dc6b1a2154";
            String topic = "topic3";
            int partition = 0;
            long offset = 2;

            // Create the TopicPartition object
            TopicPartition topicPartition = new TopicPartition(topic, partition);

            // Create the OffsetAndMetadata object
            OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(offset);

            // Create the map of topic partitions and offset metadata
            Map<TopicPartition, OffsetAndMetadata> offsetsMap = new HashMap<>();
            offsetsMap.put(topicPartition, offsetAndMetadata);

            // Reset the offsets for the specified consumer group
            AlterConsumerGroupOffsetsResult result = adminClient.alterConsumerGroupOffsets(consumerGroup, offsetsMap);
            result.all().get();

            System.out.println("Offset reset successful");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
