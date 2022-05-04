package com.sunilbainsla.kafkastreampoc.rest.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static com.sunilbainsla.kafkastreampoc.faulttolerance.config.CircuitBreakerInstances.CIRCUIT_BREAKER_INSTANCE_TOPIC_6;
import static com.sunilbainsla.kafkastreampoc.faulttolerance.config.CircuitBreakerInstances.CIRCUIT_BREAKER_INSTANCE_TOPIC_7;
import static com.sunilbainsla.kafkastreampoc.faulttolerance.config.RetryInstances.RETRY_INSTANCE_TOPIC_7;

@Service
@Slf4j
public class PocRestClient {

    private static final String SERVICE_URL = "http://localhost:6060/?message=";

    @CircuitBreaker(name = CIRCUIT_BREAKER_INSTANCE_TOPIC_6)
    public void restClient6(String message) {
        internalRestClient(message);
    }

    @CircuitBreaker(name = CIRCUIT_BREAKER_INSTANCE_TOPIC_7)
    @Retry(name = RETRY_INSTANCE_TOPIC_7)
    public void restClient7(String message) {
        internalRestClient(message);
    }

    public void internalRestClient(String message) {
        log.debug("rest call...");
        log.debug(" Making a request to {} at :{}", SERVICE_URL + message, LocalDateTime.now());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForObject(SERVICE_URL, String.class);
    }
}
