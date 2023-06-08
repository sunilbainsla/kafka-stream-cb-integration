package com.sunilbainsla.kafkastreampoc.service.events.consumers.reset;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.AlterConsumerGroupOffsetsResult;
import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.clients.admin.ConsumerGroupListing;
import org.apache.kafka.clients.admin.ListConsumerGroupOffsetsOptions;
import org.apache.kafka.clients.admin.ListConsumerGroupOffsetsResult;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.TopicPartition;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class KafkaGetAllConsumers {

    public static void main(String[] args) {
        // Set the admin client properties
        Properties adminProps = new Properties();
        adminProps.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        // Set the topic name
        String topicName = "topic3";

        // Create the AdminClient
        try (AdminClient adminClient = AdminClient.create(adminProps)) {
            // Get the list of consumer groups
            KafkaFuture<Collection<ConsumerGroupListing>> groupsFuture = adminClient.listConsumerGroups().all();
            Collection<ConsumerGroupListing> groupListings = groupsFuture.get();

            // Iterate over each consumer group
            for (ConsumerGroupListing groupListing : groupListings) {
                String groupId = groupListing.groupId();
                if (groupId.startsWith("kafka-stream-poc-topic3Consumer")) {
                    // Get the consumer group description
                    KafkaFuture<ConsumerGroupDescription> groupDescriptionFuture =
                            adminClient.describeConsumerGroups(Collections.singleton(groupId)).describedGroups().get(groupId);
                    ConsumerGroupDescription groupDescription = groupDescriptionFuture.get();

                    // Create the TopicPartition object
                    TopicPartition topicPartition = new TopicPartition(topicName, 0);

                    // Create the OffsetAndMetadata object
                    OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(3);

                    // Create the map of topic partitions and offset metadata
                    Map<TopicPartition, OffsetAndMetadata> offsetsMap = new HashMap<>();
                    offsetsMap.put(topicPartition, offsetAndMetadata);

                    // Reset the offsets for the specified consumer group
                    AlterConsumerGroupOffsetsResult result = adminClient.alterConsumerGroupOffsets(groupId, offsetsMap);
                    result.all().get();
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
