package com.sunilbainsla.kafkastreampoc.model.kafka;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class KafkaProducer {


    private final KafkaTemplate<String, Payment> kafkaTemplate;
    private final String kafkaTopicPayment;
    private final ObjectMapper objectMapper;

    private static final Logger LOG = LoggerFactory.getLogger(KafkaProducer.class);

    public KafkaProducer(KafkaTemplate<String, Payment> kafkaTemplate,
                         @Value("${topic.name.payment}") String kafkaTopicPayment, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTopicPayment = kafkaTopicPayment;
        this.objectMapper = objectMapper;
    }

    public void send(String key, Payment payload) {
        try {
            String serializedKey = UUID.randomUUID().toString();
            String serializedValue = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(kafkaTopicPayment,serializedKey,payload );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }



    }


}
