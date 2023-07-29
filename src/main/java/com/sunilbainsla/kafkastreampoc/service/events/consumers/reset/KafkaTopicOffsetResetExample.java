package com.sunilbainsla.kafkastreampoc.service.events.consumers.reset;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ConsumerGroupListing;
import org.apache.kafka.clients.admin.ConsumerGroupDescription;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.TopicPartitionInfo;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class KafkaTopicOffsetResetExample {
    public static void main(String[] args) {
        // Set the consumer group and topic for offset reset
        String consumerGroup = "kafka-stream-poc-topic3Consumer";
        String topic = "topic3";

        // Configure the Kafka AdminClient properties
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        try (AdminClient adminClient = AdminClient.create(properties)) {
            // Get the consumer group description
            KafkaFuture<ConsumerGroupDescription> consumerGroupDescriptionFuture =
                    adminClient.describeConsumerGroups(Collections.singleton(consumerGroup)).describedGroups().get(consumerGroup);
            ConsumerGroupDescription consumerGroupDescription = consumerGroupDescriptionFuture.get();

            // Get the consumer group's assigned partitions
            Set<TopicPartition> assignedPartitions = consumerGroupDescription.members()
                    .stream()
                    .flatMap(member -> member.assignment().topicPartitions().stream())
                    .collect(Collectors.toSet());

            // Create a map of NewOffsets for each assigned partition

            OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(1);
            Map<TopicPartition, OffsetAndMetadata> offsetsMap = new HashMap<>();
            //Map<TopicPartition, OffsetAndMetadata> newOffsets = new HashMap<>();
            for (TopicPartition partition : assignedPartitions) {


                offsetsMap.put(partition, offsetAndMetadata);

            }

            // Reset the offset for each assigned partition
            adminClient.alterConsumerGroupOffsets(consumerGroup, offsetsMap).all().get();

            // Verify the offset reset
            Map<TopicPartition, OffsetAndMetadata> offsets = adminClient.listConsumerGroupOffsets(consumerGroup).partitionsToOffsetAndMetadata().get();
            for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : offsets.entrySet()) {
                System.out.println("Partition: " + entry.getKey() + ", Offset: " + entry.getValue().offset());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
