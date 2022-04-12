package com.sunilbainsla.kafkastreampoc.service.events.consumers;

import com.sunilbainsla.kafkastreampoc.model.kafka.Sensor;
import com.sunilbainsla.kafkastreampoc.rest.client.PocRestClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.StreamRetryTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@Slf4j
public class TopicConsumerService {
    private final PocRestClient pocRestClient;

    public TopicConsumerService(PocRestClient pocRestClient) {
        this.pocRestClient = pocRestClient;
    }

    @Bean
    public Consumer<KStream<Object, Sensor>> topicConsumer() {
        return input -> input.foreach(this::businessLogic);
    }

    @StreamRetryTemplate
    RetryTemplate customRetryTemplate() {
        return RetryTemplate.builder()
                .maxAttempts(3)
                .fixedBackoff(1)
                .build();
    }

    private void businessLogic(Object key, Sensor val) {
        log.debug("topicConsumer: {}", val);
        customRetryTemplate().execute(
                retryContext -> {
                    log.debug("inside spring retry: {}", retryContext.getRetryCount());
                    pocRestClient.restClient(val.getId());
                    return null;
                },
                recoveryContext -> {
                    log.debug("inside spring retry recovery");
                    return null;
                });
        log.debug("topicConsumer end...");
    }
}
