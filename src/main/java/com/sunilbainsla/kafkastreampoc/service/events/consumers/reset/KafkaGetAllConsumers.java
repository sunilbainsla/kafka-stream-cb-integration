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
        String consumerGroupId="kafka-stream-poc-topic3Consumer";

        // Create the AdminClient
        try (AdminClient adminClient = AdminClient.create(adminProps)) {
            // Get the list of consumer groups
            KafkaFuture<Collection<ConsumerGroupListing>> groupsFuture = adminClient.listConsumerGroups().all();
            Collection<ConsumerGroupListing> groupListings = groupsFuture.get();

            // Iterate over each consumer group
            for (ConsumerGroupListing groupListing : groupListings) {
                String groupId = groupListing.groupId();
                if (groupId.startsWith(consumerGroupId)) {
                    ConsumerGroupDescription groupDescription = adminClient.describeConsumerGroups(Collections.singleton(consumerGroupId))
                            .describedGroups().get("kafka-stream-poc-topic3Consumer")
                            .get();

                    Map<TopicPartition, OffsetAndMetadata> offsets = adminClient.listConsumerGroupOffsets(consumerGroupId)
                            .partitionsToOffsetAndMetadata().get();

                    // Get the assigned partitions
                    Set<TopicPartition> assignedPartitions = offsets.keySet();

                    Map<TopicPartition, OffsetAndMetadata> offsetsMap = new HashMap<>();
                    OffsetAndMetadata offsetAndMetadata;
                    //for other partitions
                    int count=0;
                    for (TopicPartition partition : assignedPartitions) {
                        if(count==0) {
                             offsetAndMetadata = new OffsetAndMetadata(2);
                        }
                        else
                        {
                            offsetAndMetadata = new OffsetAndMetadata(4);
                        }
                        offsetsMap.put(partition, offsetAndMetadata);
                        ++count;

                    }
                    adminClient.alterConsumerGroupOffsets(groupId, offsetsMap).all().get();
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
