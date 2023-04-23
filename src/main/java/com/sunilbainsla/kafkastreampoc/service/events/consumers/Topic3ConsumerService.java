package com.sunilbainsla.kafkastreampoc.service.events.consumers;

import com.sunilbainsla.kafkastreampoc.controllers.RestartController;
import com.sunilbainsla.kafkastreampoc.model.kafka.Payment;
import com.sunilbainsla.kafkastreampoc.rest.client.PocRestClient;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Slf4j
/**
 * Retries with jitter
 */
@Component
public class Topic3ConsumerService {
  @Autowired
  private RestartController restartController;
  private final PocRestClient pocRestClient;


  public Topic3ConsumerService(PocRestClient pocRestClient) {
    this.pocRestClient = pocRestClient;
  }

  @Bean
  public Consumer<KStream<Object, Payment>> topic3Consumer() {
    return input -> input.foreach(this::businessLogic);
  }

  private void businessLogic(Object key, Payment val) {
    System.out.println("before re start: {}"+ val);
    if(null!=val.getMessage() &&val.getMessage().equalsIgnoreCase("Sunil")){
    restartController.restart();
    }
    System.out.println("topic3Consumer end...");
  }
}
