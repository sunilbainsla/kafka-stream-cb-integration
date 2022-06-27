package com.sunilbainsla.kafkastreampoc.service.events.suppliers;

import com.sunilbainsla.kafkastreampoc.model.kafka.Account;
import com.sunilbainsla.kafkastreampoc.model.kafka.Sensor;
import com.sunilbainsla.kafkastreampoc.model.kafka.Payment;
import com.sunilbainsla.kafkastreampoc.model.request.Payments;
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

  public void topicPublisher(String id, Payments payments) {
    Payment payment = new Payment();
    payment.setId(UUID.randomUUID() + payments.getId());
    payment.setMessage(payments.getMessage());
    String publisherTopic = "topic" + id;
    if(id.equalsIgnoreCase("9"))
    {
      payment.setReference(payments.getReference());
      payment.setCurrency(payments.getCurrency());
      payment.setNumericReference(payments.getNumericReference());
      payment.setAmount(payments.getAmount());
      Account  account =new Account();
      account.setAccountName(payments.getDebtorAccount().getAccountName());
      payment.setDebtorAccount(account);
      publisherTopic = "payment-processor";
    }
    log.debug("Producer {}: {}", publisherTopic, payment);
    streamBridge.send(publisherTopic, payment);
  }
}
