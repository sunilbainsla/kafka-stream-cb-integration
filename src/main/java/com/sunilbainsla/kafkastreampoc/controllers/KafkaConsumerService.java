package com.sunilbainsla.kafkastreampoc.controllers;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConsumerAwareErrorHandler;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer2;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer2.VALUE_DESERIALIZER_CLASS;

import java.util.HashMap;
import java.util.Map;

public class KafkaConsumerService {

    @Autowired
    private ConsumerFactory<String, String> consumerFactory;

    @KafkaListener(topics = "your_topic")
    public void listen(ConsumerRecord<String, String> record) {
        // Your message processing logic here
        System.out.println("Received Message: " + record.value());
    }

    @KafkaListener(topics = "your_topic", errorHandler = "customErrorHandler")
    public void listenWithCustomErrorHandler(ConsumerRecord<String, String> record) {
        // Your message processing logic here
        System.out.println("Received Message: " + record.value());
    }

    @Bean
    public ConsumerAwareErrorHandler customErrorHandler() {
        // Custom error handler to handle deserialization errors
        return new SeekToCurrentErrorHandler(new CustomDeserializationExceptionHandler());
    }

    private static class CustomDeserializationExceptionHandler implements ConsumerAwareListenerErrorHandler {

        @Override
        public Object handleError(ConsumerRecord<?, ?> record, ListenerExecutionFailedException exception, Consumer<?, ?> consumer) {
            // Log or handle the deserialization error
            System.err.println("Deserialization error for record: " + record.value());

            // Manually commit the offset to avoid losing track
            consumer.commitSync();

            // Return a custom result or null
            return null;
        }
    }

    // Additional configuration for the consumer factory
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "your_bootstrap_servers");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "your_consumer_group_id");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer2.class.getName());
        props.put(VALUE_DESERIALIZER_CLASS, ErrorHandlingDeserializer2.class.getName());
        props.put(ErrorHandlingDeserializer2.VALUE_DESERIALIZER_CLASS, StringDeserializer.class.getName());

        return new DefaultKafkaConsumerFactory<>(props);
    }
}
