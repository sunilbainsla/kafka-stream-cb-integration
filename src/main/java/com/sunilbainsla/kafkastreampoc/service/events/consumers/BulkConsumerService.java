package com.sunilbainsla.kafkastreampoc.service.events.consumers;

import com.sunilbainsla.kafkastreampoc.model.kafka.KafkaProducer;
import com.sunilbainsla.kafkastreampoc.model.kafka.Payment;
import com.sunilbainsla.kafkastreampoc.rest.client.PocRestClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@Slf4j
public class BulkConsumerService {

  private final KafkaProducer kafkaProducer;
  public BulkConsumerService(KafkaProducer kafkaProducer) {
    this.kafkaProducer = kafkaProducer;

  }

  @Bean
  public Consumer<KStream<Object, Payment>> bulkConsumer() {
    return input -> input.foreach(this::businessLogic);
  }

  private void businessLogic(Object key, Payment val) {
    log.debug("bulk start: {}", val);
    kafkaProducer.send(val.getPaymentId(), val);
    log.debug("bulk end...");
  }
}
