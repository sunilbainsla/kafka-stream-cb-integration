package com.sunilbainsla.kafkastreampoc.service.events.consumers.batch;

import com.sunilbainsla.kafkastreampoc.kstream.Employee;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryConfig;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;

@Configuration
@EnableBatchProcessing
@EnableRetry
public class BatchConfiguration {
    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    private Step chunkStep;
    @Bean
    public Step chunkStep(
            ItemReader<Employee> reader,
            ItemProcessor<Employee, Employee> processor,
            ItemWriter<Employee> writer,
            Retry myRetry,
            CircuitBreaker chunkCircuitBreaker
    ) {
        ItemWriter<Employee> retryWriter = new RetryItemWriter<>(writer,myRetry);
        ItemProcessor<Employee, Employee> retryProcessor = new RetryItemProcessor<>(processor, myRetry);

        ItemProcessor<Employee, Employee> circuitBreakerProcessor = new CircuitBreakerItemProcessor(chunkCircuitBreaker, retryProcessor);
        ItemWriter<Employee> circuitBreakerWriter = new CircuitBreakerItemWriter( retryWriter,chunkCircuitBreaker);

        return stepBuilderFactory.get("chunkStep")
                .<Employee, Employee>chunk(10)
                .reader(reader)
                .processor(circuitBreakerProcessor)
                .writer(circuitBreakerWriter)
                .faultTolerant()
                .retryLimit(3)
                .retry(Exception.class)
                .skipLimit(10)
                .skip(Exception.class)
                .build();
    }

    @Bean
    public Job importUserJob(JobExecutionListener listener) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(chunkStep) // Reference the chunkStep bean here
                .end()
                .build();
    }
    @Bean
    public RetryConfig retryConfig() {
        return RetryConfig.custom()
                .maxAttempts(3)
                .build();
    }

    @Bean
    public Retry retry(RetryConfig retryConfig) {
        return Retry.of("myRetry", retryConfig);
    }

    @Bean
    public CircuitBreakerConfig circuitBreakerConfig() {
        return CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(10)
                .minimumNumberOfCalls(5)
                .build();
    }

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        return CircuitBreakerRegistry.ofDefaults();
    }

    @Bean
    public CircuitBreaker chunkCircuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry) {
        return circuitBreakerRegistry.circuitBreaker("chunkCircuitBreaker", circuitBreakerConfig());
    }
}
