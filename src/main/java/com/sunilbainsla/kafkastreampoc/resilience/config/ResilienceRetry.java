package com.sunilbainsla.kafkastreampoc.resilience.config;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClientException;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;

@Configuration
@AllArgsConstructor
@Slf4j
public class ResilienceRetry {

    public static final String RETRY_INSTANCE_TOPIC_6 = "retry-instance-topic6";
    private final RetryRegistry retryRegistry;
    private final CircuitBreaker circuitBreakerInstanceTopic6;

    public RetryConfig defaultRetryConfig(List<CircuitBreaker> circuitBreakers) {
        return RetryConfig
                .custom()
                .maxAttempts(10)
                .retryExceptions(RestClientException.class,
                        CallNotPermittedException.class)
                .intervalBiFunction(
                        (integer, objects) -> {
                            long duration = Duration.ofSeconds(1).toMillis();
                            long cbDuration = circuitBreakers
                                    .stream()
                                    .filter(circuitBreaker -> !circuitBreaker.tryAcquirePermission())
                                    .map(circuitBreaker -> circuitBreaker.getCircuitBreakerConfig()
                                            .getWaitIntervalFunctionInOpenState().apply(integer) + 1000L)
                                    .max(Comparator.comparingLong(Long::longValue))
                                    .orElse(0L);
                            final var maxDuration = Math.max(duration, cbDuration);
                            log.debug("retry back-off: {}, {}ms", integer, maxDuration);
                            return maxDuration;
                        }
                )
                .build();
    }

    @Bean
    public Retry retryInstanceTopic6() {
        List<CircuitBreaker> circuitBreakers = List.of(circuitBreakerInstanceTopic6);
        RetryConfig retryConfig = defaultRetryConfig(circuitBreakers);
        return retryRegistry.retry(RETRY_INSTANCE_TOPIC_6, retryConfig);
    }
}
