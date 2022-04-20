package com.sunilbainsla.kafkastreampoc.service.events.consumers;

import com.sunilbainsla.kafkastreampoc.model.kafka.Topic3Message;
import com.sunilbainsla.kafkastreampoc.rest.client.PocRestClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@Slf4j
public class Topic5ConsumerService {
    private final PocRestClient pocRestClient;

    public Topic5ConsumerService(PocRestClient pocRestClient) {
        this.pocRestClient = pocRestClient;
    }

    @Bean
    public Consumer<KStream<Object, Topic3Message>> topic5Consumer() {
        return input -> input.foreach(this::businessLogic);
    }

    private void businessLogic(Object key, Topic3Message val) {
        log.debug("topic5Consumer start: {}", val);
        log.debug("topic5Consumer end...");
         pocRestClient.restClient5(val.getMessage());

    }
}
