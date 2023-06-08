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
import java.util.stream.Collectors;

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
                    ConsumerGroupDescription consumerGroupDescription = groupDescriptionFuture.get();


                    // Get the consumer group's assigned partitions
                    Set<TopicPartition> assignedPartitions = consumerGroupDescription.members()
                            .stream()
                            .flatMap(member -> member.assignment().topicPartitions().stream())
                            .collect(Collectors.toSet());
                  // Default partition
                    TopicPartition topicPartition = new TopicPartition(topicName, 0);
                    OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(2);
                    Map<TopicPartition, OffsetAndMetadata> offsetsMap = new HashMap<>();
                    offsetsMap.put(topicPartition, offsetAndMetadata);

                    //for other partitions
                    for (TopicPartition partition : assignedPartitions) {


                        offsetsMap.put(partition, offsetAndMetadata);

                    }
                    adminClient.alterConsumerGroupOffsets(groupId, offsetsMap).all().get();
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
