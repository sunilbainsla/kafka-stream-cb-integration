package com.sunilbainsla.kafkastreampoc.resilience.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.common.retry.configuration.RetryConfigCustomizer;
import io.github.resilience4j.core.IntervalFunction;

@Configuration
public class CircuitBreakerCustomizer {
    public IntervalFunction jitterExponentialFunction() {
        return IntervalFunction.ofExponentialRandomBackoff(Duration.ofSeconds(5), 2, 0.6D);
    }

    @Bean
    public RetryConfigCustomizer retryInstanceTopic3Customizer() {
        return RetryConfigCustomizer
                .of("retry-instance-topic3", builder -> builder.intervalFunction(jitterExponentialFunction()));
    }
}
