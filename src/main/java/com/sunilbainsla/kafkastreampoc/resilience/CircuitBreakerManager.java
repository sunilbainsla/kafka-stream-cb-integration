package com.sunilbainsla.kafkastreampoc.resilience;

import com.sunilbainsla.kafkastreampoc.resilience.config.CircuitBreakerConfiguration;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnStateTransitionEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;
import org.springframework.cloud.stream.binding.BindingsLifecycleController;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.config.StreamsBuilderFactoryBeanConfigurer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Optional;

import static io.github.resilience4j.circuitbreaker.CircuitBreaker.StateTransition.*;

@Component
@Slf4j
public class CircuitBreakerManager {

    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final BindingsLifecycleController bindingsController;
    private final CircuitBreakerConfiguration circuitBreakerConfiguration;

    public CircuitBreakerManager(CircuitBreakerRegistry circuitBreakerRegistry,
                                 BindingsLifecycleController bindingsController,
                                 CircuitBreakerConfiguration circuitBreakerConfiguration) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
        this.bindingsController = bindingsController;
        this.circuitBreakerConfiguration = circuitBreakerConfiguration;
    }

    @PostConstruct
    public void postConstruct() {
        circuitBreakerRegistry
                .getAllCircuitBreakers()
                .forEach(circuitBreaker -> circuitBreaker.getEventPublisher()
                        .onStateTransition(this::controlStateTransition));
    }

    private void controlStateTransition(CircuitBreakerOnStateTransitionEvent event) {
        log.info("{} circuit breaker: {}", event.getCircuitBreakerName(), event.getStateTransition());

        Optional
                .ofNullable(circuitBreakerConfiguration.getInstances())
                .map(map -> map.get(event.getCircuitBreakerName()))
                .orElse(Collections.emptyList())
                .forEach(
                        consumer -> {
                            final var stateTransition = event.getStateTransition();
                            if (stateTransition.equals(CLOSED_TO_OPEN) ||
                                    stateTransition.equals(HALF_OPEN_TO_OPEN)) {
                                bindingsController.stop(consumer);
                                log.info("State of {}: STOPPED", consumer);
                            } else if (stateTransition.equals(OPEN_TO_HALF_OPEN)) {
                                bindingsController.start(consumer);
                                log.info("State of {}: STARTED", consumer);
                            }
                        }
                );
    }


    @Bean
    StreamsBuilderFactoryBeanConfigurer streamsCustomizer() {
        return new StreamsBuilderFactoryBeanConfigurer() {

            @Override
            public void configure(StreamsBuilderFactoryBean factoryBean) {
                factoryBean
                        .setKafkaStreamsCustomizer(kafkaStreams -> kafkaStreams
                                .setUncaughtExceptionHandler(exception ->
                                        StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse.REPLACE_THREAD));
            }

            @Override
            public int getOrder() {
                return Integer.MAX_VALUE;
            }

        };
    }
}
