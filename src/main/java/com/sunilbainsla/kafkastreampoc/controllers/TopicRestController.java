package com.sunilbainsla.kafkastreampoc.controllers;

import com.sunilbainsla.kafkastreampoc.model.request.Payments;
import com.sunilbainsla.kafkastreampoc.service.events.suppliers.StreamBridgeProducersService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/")
@Slf4j
@AllArgsConstructor
public class TopicRestController {

    private final StreamBridgeProducersService streamBridgeProducersService;

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<String> postTopicMessage(@PathVariable(name = "id") String id,
                                         @RequestBody(required = false) Payments request) {
        if (id.equals("1")) {
            log.debug("postTopic");
            streamBridgeProducersService.topicPublisher();
        } else {
            log.debug("postTopic{}", id);
            streamBridgeProducersService.topicPublisher(id, request);
        }
        return Mono.just("ok");
    }
}
