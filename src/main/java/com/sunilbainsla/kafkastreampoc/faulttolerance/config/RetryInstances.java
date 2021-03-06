package com.sunilbainsla.kafkastreampoc.faulttolerance.config;

import com.sunilbainsla.kafkastreampoc.faulttolerance.retry.backoff.FixedBackOffPolicyWithCb;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamRetryTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestClientException;

import java.time.Duration;
import java.util.Optional;

@Configuration
@AllArgsConstructor
@Slf4j
public class RetryInstances {

    public static final String RETRY_INSTANCE_TOPIC_7 = "retry-instance-topic7";
    private final RetryRegistry retryRegistry;
    private final CircuitBreaker circuitBreakerInstanceTopic6;
    private final CircuitBreaker circuitBreakerInstanceTopic7;
    private final CircuitBreaker circuitBreakerInstanceTopic8;

    public RetryConfig defaultRetryConfig(CircuitBreaker circuitBreaker) {
        return RetryConfig
                .custom()
                .maxAttempts(10)
                .retryExceptions(RestClientException.class,
                        CallNotPermittedException.class)
                .intervalBiFunction(
                        (integer, objects) -> {
                            long duration = Duration.ofSeconds(1).toMillis();
                            long cbDuration = Optional.ofNullable(circuitBreaker)
                                    .filter(cb -> !cb.tryAcquirePermission())
                                    .map(cb -> cb.getCircuitBreakerConfig()
                                            .getWaitIntervalFunctionInOpenState().apply(integer) + 1000L)
                                    .orElse(0L);
                            final var maxDuration = Math.max(duration, cbDuration);
                            log.debug("retry back-off: {}, {}ms", integer, maxDuration);
                            return maxDuration;
                        }
                )
                .build();
    }

    @Bean
    @StreamRetryTemplate
    public RetryTemplate retryInstanceTopic6() {
        return RetryTemplate.builder()
                .infiniteRetry()
                .customBackoff(new FixedBackOffPolicyWithCb(Duration.ofSeconds(1),
                        circuitBreakerInstanceTopic6))
                .build();
    }

    @Bean
    public Retry retryInstanceTopic7() {
        RetryConfig retryConfig = defaultRetryConfig(circuitBreakerInstanceTopic7);
        return retryRegistry.retry(RETRY_INSTANCE_TOPIC_7, retryConfig);
    }

    @Bean
    @StreamRetryTemplate
    public RetryTemplate retryInstanceTopic8() {
        return RetryTemplate.builder()
                .withinMillis(Duration.ofMinutes(2).toMillis())
                .customBackoff(new FixedBackOffPolicyWithCb(Duration.ofSeconds(1),
                        circuitBreakerInstanceTopic8))
                .build();
    }
}
