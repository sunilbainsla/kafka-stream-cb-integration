package com.sunilbainsla.kafkastreampoc.service.events.suppliers;

import com.sunilbainsla.kafkastreampoc.model.kafka.Sensor;
import com.sunilbainsla.kafkastreampoc.model.kafka.TopicMessage;
import com.sunilbainsla.kafkastreampoc.model.request.TopicRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

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
    log.debug("Producer topic1: {}", sensor);
    streamBridge.send("topic1", sensor);
  }

  public void topicPublisher(String id, TopicRequest topicRequest) {
    TopicMessage topicMessage = new TopicMessage();
    topicMessage.setId(UUID.randomUUID() + "-v1");
    topicMessage.setMessage(topicRequest.getMessage());
    String publisherTopic = "topic" + id;
    if(id.equalsIgnoreCase("9"))
    {
      publisherTopic = "payment-processor";
    }
    log.debug("Producer {}: {}", publisherTopic, topicMessage);
    streamBridge.send(publisherTopic, topicMessage);
  }
}
