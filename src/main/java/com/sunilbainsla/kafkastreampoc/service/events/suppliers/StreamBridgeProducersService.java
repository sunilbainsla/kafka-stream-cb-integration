package com.sunilbainsla.kafkastreampoc.service.events.suppliers;

import com.sunilbainsla.kafkastreampoc.model.kafka.Sensor;
import com.sunilbainsla.kafkastreampoc.model.kafka.Payment;
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

  public void topicPublisher(String id, Payment payment) {
//    PaymentNew paymentNew = new PaymentNew();
//    paymentNew.setPaymentId(payment.getPaymentId());
//    paymentNew.setMessage(payment.getMessage());
    String publisherTopic = "topic" + id;
    if(id.equalsIgnoreCase("9"))
    {
//      paymentNew.setReference(payment.getReference());
//      paymentNew.setCurrency(payment.getCurrency());
//      paymentNew.setNumericReference(payment.getNumericReference());
//      paymentNew.setAmount(payment.getAmount());
//      Account  account =new Account();
//      account.setAccountName(payment.getDebtorAccount().getAccountName());
//      account.setSchemeName(payment.getDebtorAccount().getSchemeName());
//      account.setIdentification(payment.getDebtorAccount().getIdentification());
//      Account  creditorAccount =new Account();
//      creditorAccount.setAccountName(payment.getCreditorAccount().getAccountName());
//      creditorAccount.setSchemeName(payment.getCreditorAccount().getSchemeName());
//      creditorAccount.setIdentification(payment.getCreditorAccount().getIdentification());
//      paymentNew.setDebtorAccount(account);
//      paymentNew.setCreditorAccount(creditorAccount);
      publisherTopic = "payment-processor";
    }
    log.debug("Producer {}: {}", publisherTopic, payment);
    streamBridge.send(publisherTopic, payment);
  }
}
