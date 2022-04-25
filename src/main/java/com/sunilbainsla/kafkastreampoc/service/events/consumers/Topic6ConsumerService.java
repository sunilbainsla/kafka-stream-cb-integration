package com.sunilbainsla.kafkastreampoc.service.events.consumers;

import com.sunilbainsla.kafkastreampoc.model.kafka.TopicMessage;
import com.sunilbainsla.kafkastreampoc.rest.client.PocRestClient;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.binder.RequeueCurrentMessageException;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Topic6ConsumerService {

  private final PocRestClient pocRestClient;

  public Topic6ConsumerService(PocRestClient pocRestClient) {
    this.pocRestClient = pocRestClient;
  }

  @Bean
  public Consumer<KStream<Object, TopicMessage>> topic6Consumer() {
    return input -> input.foreach(this::businessLogic);
  }

  private void businessLogic(Object key, TopicMessage val) {
    log.debug("topic6Consumer: {}", val);
    try {
      pocRestClient.restClient6(val.getMessage());
    } catch (Exception e) {
      throw new RequeueCurrentMessageException(e);
    }
    log.debug("topic6Consumer end...");
  }
}
