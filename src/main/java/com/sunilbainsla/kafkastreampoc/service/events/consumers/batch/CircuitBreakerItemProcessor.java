package com.sunilbainsla.kafkastreampoc.service.events.consumers.batch;

import com.sunilbainsla.kafkastreampoc.kstream.Employee;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.springframework.batch.item.ItemProcessor;

public class CircuitBreakerItemProcessor<Employee, S> implements ItemProcessor<Employee, Employee> {

    private final ItemProcessor<Employee, Employee> delegate;
    private final CircuitBreaker chunkCircuitBreaker;
    public CircuitBreakerItemProcessor(CircuitBreaker chunkCircuitBreaker, ItemProcessor<Employee, Employee> delegate) {
        this.delegate = delegate;
        this.chunkCircuitBreaker = chunkCircuitBreaker;
}

    @Override
    public Employee process(Employee item) throws Exception {
        return chunkCircuitBreaker.executeSupplier(() -> {
            try {
                return delegate.process(item);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
