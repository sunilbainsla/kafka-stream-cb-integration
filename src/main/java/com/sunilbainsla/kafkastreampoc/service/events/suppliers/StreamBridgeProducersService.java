package com.sunilbainsla.kafkastreampoc.service.events.suppliers;

import java.util.Random;
import java.util.UUID;

import com.sunilbainsla.kafkastreampoc.model.kafka.Sensor;
import com.sunilbainsla.kafkastreampoc.model.kafka.TopicMessage;
import com.sunilbainsla.kafkastreampoc.model.request.TopicRequest;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StreamBridgeProducersService {
    private final StreamBridge streamBridge;
    private final Random random;

    public StreamBridgeProducersService(StreamBridge streamBridge) {
        this.random = new Random();
        this.streamBridge = streamBridge;
    }

    public void topicPublisher() {
        Sensor sensor = new Sensor();
        sensor.setId(UUID.randomUUID() + "-v1");
        sensor.setAcceleration(random.nextFloat() * 10);
        sensor.setVelocity(random.nextFloat() * 100);
        sensor.setTemperature(random.nextFloat() * 50);
        log.debug("topicPublisher: {}", sensor);
        streamBridge.send("topicProducer-out-0", sensor);
    }

    public void topicPublisher(String id, TopicRequest topicRequest) {
        TopicMessage topicMessage = new TopicMessage();
        topicMessage.setId(UUID.randomUUID() + "-v1");
        topicMessage.setMessage(topicRequest.getMessage());
        String publisher = String.format("topic%sPublisher", id);
        String publisherBinding = String.format("%-out-0", publisher);
        log.debug("{}: {}", publisher, topicMessage);
        streamBridge.send(publisherBinding, topicMessage);
    }
}
