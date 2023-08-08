package com.sunilbainsla.kafkastreampoc.service.events.consumers.batch;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class CircuitBreakerItemWriter<Employee> implements ItemWriter<Employee> {

    private final ItemWriter<Employee> delegate;
    private final CircuitBreaker chunkCircuitBreaker;

    public CircuitBreakerItemWriter(ItemWriter<Employee> delegate, CircuitBreaker chunkCircuitBreaker) {
        this.delegate = delegate;
        this.chunkCircuitBreaker = chunkCircuitBreaker;
    }

    @Override
    public void write(List<? extends Employee> items) throws Exception {
        chunkCircuitBreaker.executeRunnable(() -> {
            try {
                delegate.write(items);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
