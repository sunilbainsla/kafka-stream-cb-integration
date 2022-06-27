package com.sunilbainsla.kafkastreampoc.service.events.consumers;

import com.sunilbainsla.kafkastreampoc.model.kafka.Payment;
import com.sunilbainsla.kafkastreampoc.rest.client.PocRestClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@AllArgsConstructor
@Slf4j
public class Topic8ConsumerService {

    private final PocRestClient pocRestClient;
    private final RetryTemplate retryInstanceTopic8;

    @Bean
    public Consumer<KStream<Object, Payment>> topic8Consumer() {
        return input -> input.foreach(this::businessLogic);
    }

    private void businessLogic(Object key, Payment val) {
        log.debug("topic8Consumer: {}", val);
        retryInstanceTopic8.execute(context -> {
            pocRestClient.restClient8(val.getMessage());
            return null;
        }, context -> {
            log.debug("time-out...");
            log.debug("producer logic goes here...");
            return null;
        });
        log.debug("topic8Consumer end...");
    }
}
