package com.sunilbainsla.kafkastreampoc.service.events.consumers;

import com.sunilbainsla.kafkastreampoc.model.kafka.Payment;
import com.sunilbainsla.kafkastreampoc.rest.client.PocRestClient;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RateLimiterConsumerService4 {

  private final PocRestClient pocRestClient;

  public RateLimiterConsumerService4(PocRestClient pocRestClient) {
    this.pocRestClient = pocRestClient;
  }

  @Bean
  public Consumer<KStream<Object, Payment>> topic4Consumer() {
    return input -> input.foreach(this::businessLogic);
  }

  private void businessLogic(Object key, Payment val) {
    log.debug("topic4Consumer start: {}", val);
    pocRestClient.restClient4(val.getMessage());
    log.debug("topic4Consumer end...");
  }
}
