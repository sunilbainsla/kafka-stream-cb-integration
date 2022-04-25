package com.sunilbainsla.kafkastreampoc.service.events.consumers;

import com.sunilbainsla.kafkastreampoc.model.kafka.Sensor;
import com.sunilbainsla.kafkastreampoc.rest.client.PocRestClient;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.StreamRetryTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SimpleRetryConsumerService1 {

  private final PocRestClient pocRestClient;

  public SimpleRetryConsumerService1(PocRestClient pocRestClient) {
    this.pocRestClient = pocRestClient;
  }

  @Bean
  public Consumer<KStream<Object, Sensor>> topicConsumer() {
    return input -> input.foreach(this::businessLogic);
  }

  private RetryTemplate customRetryTemplate() {
    return RetryTemplate.builder().maxAttempts(3).fixedBackoff(1).build();
  }

  private void businessLogic(Object key, Sensor val) {
    log.debug("Consumer 1 start : {}", val);
    customRetryTemplate()
      .execute(
        retryContext -> {
          log.debug(
            "Consumer 1 with retries: {}",
            retryContext.getRetryCount()
          );
          pocRestClient.restClient(val.getId());
          return null;
        },
        recoveryContext -> {
          log.debug("fallback  recovery logic after some retries");
          return null;
        }
      );
    log.debug("Consumer  1 end...");
  }
}
