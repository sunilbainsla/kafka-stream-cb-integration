package com.sunilbainsla.kafkastreampoc.service.events.consumers;

import com.sunilbainsla.kafkastreampoc.model.kafka.Payment;
import com.sunilbainsla.kafkastreampoc.rest.client.PocRestClient;

import java.util.function.Consumer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class Topic6ConsumerService {
// infinite retry without rebalancing
    private final PocRestClient pocRestClient;
    private final RetryTemplate retryInstanceTopic6;

    @Bean
    public Consumer<KStream<Object, Payment>> topic6Consumer() {
        return input -> input.foreach(this::businessLogic);
    }

    private void businessLogic(Object key, Payment val) {
        log.debug("topic6Consumer: {}", val);
        retryInstanceTopic6.execute(context -> {
            pocRestClient.restClient6(val.getMessage());
            return null;
        });
        log.debug("topic6Consumer end...");
    }
}
