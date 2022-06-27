package com.sunilbainsla.kafkastreampoc.service.events.consumers;

import com.sunilbainsla.kafkastreampoc.model.kafka.Payment;
import com.sunilbainsla.kafkastreampoc.rest.client.PocRestClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@AllArgsConstructor
@Slf4j
public class Topic7ConsumerService {
    // 10 retry without rebalancing and fallback
    private final PocRestClient pocRestClient;

    @Bean
    public Consumer<KStream<Object, Payment>> topic7Consumer() {
        return input -> input.foreach(this::businessLogic);
    }

    private void businessLogic(Object key, Payment val) {
        log.debug("topic7Consumer: {}", val);
        try {
            pocRestClient.restClient7(val.getMessage());
        } catch (Exception ignored) {
           // throw new RequeueCurrentMessageException("test exception"); for REPLACE_THREAD
           log.debug("Producer goes here...");
        }
        log.debug("topic7Consumer end...");
    }
}
