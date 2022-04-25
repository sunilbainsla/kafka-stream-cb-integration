package com.sunilbainsla.kafkastreampoc;

import io.github.resilience4j.common.CompositeCustomizer;
import io.github.resilience4j.common.retry.configuration.RetryConfigCustomizer;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
@Slf4j
public class KafkaStreamPocApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaStreamPocApplication.class, args);
    }
}