package com.sunilbainsla.kafkastreampoc.service.events.consumers;

import com.sunilbainsla.kafkastreampoc.model.kafka.TopicMessage;
import com.sunilbainsla.kafkastreampoc.rest.client.PocRestClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.binder.RequeueCurrentMessageException;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@AllArgsConstructor
@Slf4j
public class Topic7ConsumerService {

    private final PocRestClient pocRestClient;

    @Bean
    public Consumer<KStream<Object, TopicMessage>> topic7Consumer() {
        return input -> input.foreach(this::businessLogic);
    }

    private void businessLogic(Object key, TopicMessage val) {
        log.debug("topic7Consumer: {}", val);
        pocRestClient.restClient7(val.getMessage());
        log.debug("topic7Consumer end...");
    }
}
