package com.sunilbainsla.kafkastreampoc.faulttolerance.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClientException;

import java.time.Duration;

@Configuration
@AllArgsConstructor
@Slf4j
public class CircuitBreakerInstances {

    public static final String CIRCUIT_BREAKER_INSTANCE_TOPIC_6 = "circuit-breaker-instance-topic6";
    public static final String CIRCUIT_BREAKER_INSTANCE_TOPIC_7 = "circuit-breaker-instance-topic7";
    public static final String CIRCUIT_BREAKER_INSTANCE_TOPIC_8 = "circuit-breaker-instance-topic8";

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    @Bean
    public CircuitBreakerConfig defaultCircuitBreakerConfig() {
        return CircuitBreakerConfig
                .custom()
                .failureRateThreshold(60)
                .waitDurationInOpenState(Duration.ofSeconds(60))
                .minimumNumberOfCalls(3)
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(10)
                .recordExceptions(RestClientException.class)
                .build();
    }

    @Bean
    public CircuitBreaker circuitBreakerInstanceTopic6(CircuitBreakerConfig defaultCircuitBreakerConfig) {
        var cb = circuitBreakerRegistry.circuitBreaker(CIRCUIT_BREAKER_INSTANCE_TOPIC_6, defaultCircuitBreakerConfig);
        cb.getEventPublisher().onStateTransition(event ->
                log.info("{} circuit breaker: {}", event.getCircuitBreakerName(), event.getStateTransition()));
        return cb;
    }

    @Bean
    public CircuitBreaker circuitBreakerInstanceTopic7(CircuitBreakerConfig defaultCircuitBreakerConfig) {
        var cb = circuitBreakerRegistry.circuitBreaker(CIRCUIT_BREAKER_INSTANCE_TOPIC_7, defaultCircuitBreakerConfig);
        cb.getEventPublisher().onStateTransition(event ->
                log.info("{} circuit breaker: {}", event.getCircuitBreakerName(), event.getStateTransition()));
        return cb;
    }

    @Bean
    public CircuitBreaker circuitBreakerInstanceTopic8(CircuitBreakerConfig defaultCircuitBreakerConfig) {
        var cb = circuitBreakerRegistry.circuitBreaker(CIRCUIT_BREAKER_INSTANCE_TOPIC_8, defaultCircuitBreakerConfig);
        cb.getEventPublisher().onStateTransition(event ->
                log.info("{} circuit breaker: {}", event.getCircuitBreakerName(), event.getStateTransition()));
        return cb;
    }
}
