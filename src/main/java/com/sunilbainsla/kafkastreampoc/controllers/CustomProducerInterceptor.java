package com.sunilbainsla.kafkastreampoc.controllers;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.Cluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class CustomProducerInterceptor implements ProducerInterceptor {


    private Map<String, ProducerRecord<String, String>> recordMap = new HashMap<>();

    @Autowired
    private KafkaTemplate kafkaTemplate;
    private static final int MAX_RETRY_ATTEMPTS = 3;
    private int retryCount = 0;

    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord record) {
        recordMap.put(record.key().toString(), record);
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        if (exception != null) {
            log.error("Error while sending message to Kafka topic: {}", metadata.topic(), exception);
            if (retryCount < MAX_RETRY_ATTEMPTS) {
                retryCount++;
                log.info("Retrying message for the {} time", retryCount);
                ProducerRecord<String, String> originalRecord = recordMap.get(metadata.serializedKeySize());

                // Resend the message using the KafkaTemplate
                if (originalRecord != null) {
                    kafkaTemplate.send(originalRecord);
                }
                log.info("Message resent to topic: {}, partition: {}, offset: {}", metadata.topic(), metadata.partition(), metadata.offset());

            } else {
                log.error("Max retry attempts reached for message. Failed to send message to Kafka topic: {}", metadata.topic(), exception);
                // TODO: Implement logic to raise an alert or log the final error
            }
        }
    }

    @Override
    public void close() {
        // Close any resources used by the interceptor
    }

    @Override
    public void configure(Map<String, ?> configs) {
        // Configure any properties required by the interceptor
    }
}
