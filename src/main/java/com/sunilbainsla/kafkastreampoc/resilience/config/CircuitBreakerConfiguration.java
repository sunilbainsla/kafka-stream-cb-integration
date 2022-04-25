package com.sunilbainsla.kafkastreampoc.resilience.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties("circuit-breaker")
public class CircuitBreakerConfiguration {
    private Map<String, List<String>> instances;
}
