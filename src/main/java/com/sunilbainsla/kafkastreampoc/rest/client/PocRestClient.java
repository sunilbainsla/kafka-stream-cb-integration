package com.sunilbainsla.kafkastreampoc.rest.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.common.retry.configuration.RetryConfigCustomizer;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class PocRestClient {

    private static final String SERVICE_URL = "http://localhost:6060/?message=";
    private static final String CB_INSTANCE = "cb-instance-topic";
    private static final String RETRY_INSTANCE = "retry-instance-topic";
    private static final String CB_INSTANCE2 = "cb-instance-topic2";
    private static final String RETRY_INSTANCE2 = "retry-instance-topic2";
    private static final String CB_INSTANCE3 = "cb-instance-topic3";
    private static final String RETRY_INSTANCE3 = "retry-instance-topic3";
    private static final String CB_INSTANCE4 = "cb-instance-topic4";
    private static final String RETRY_INSTANCE4 = "retry-instance-topic4";
    private static final String RATE_INSTANCE ="rate-instance";
    private static final String CB_INSTANCE5 = "cb-instance-topic5";
    private static final String RETRY_INSTANCE5 = "retry-instance-topic5";
    private static final String TIME_INSTANCE ="time-instance";


    //Certain retries and fallback offset commit
    //    @CircuitBreaker(name = CB_INSTANCE)
    //    @Retry(name = RETRY_INSTANCE)
    public void restClient(String message) {
        internalRestClient(message);
    }

    // With certain retry and circuit breaker
    @CircuitBreaker(name = CB_INSTANCE2)
    @Retry(name = RETRY_INSTANCE2)
    public void restClient2(String message) {
        internalRestClient(message);
    }
    // with exponential backoff policy and Jitter
    @CircuitBreaker(name = CB_INSTANCE3)
   @Retry(name = RETRY_INSTANCE3)
    public void restClient3(String message) {
        internalRestClient(message);
    }

    // With certain retry and circuit breaker
  //  @CircuitBreaker(name = CB_INSTANCE4)
   // @Retry(name = RETRY_INSTANCE4)
    @RateLimiter(name = RATE_INSTANCE)
    public void restClient4(String message) {
        internalRestClient(message);
    }
    // With certain retry and circuit breaker
    @CircuitBreaker(name = CB_INSTANCE5)
    @Retry(name = RETRY_INSTANCE5)
   // @TimeLimiter(name = TIME_INSTANCE ,fallbackMethod = "defaultConfirmPayment")
    public void restClient5(String message) {
        internalRestClient(message);
    }
    public void internalRestClient(String message) {
        log.debug("rest call...");
        log.debug(" Making a request to {} at :{}", SERVICE_URL + message, LocalDateTime.now());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForObject(SERVICE_URL, String.class);
    }
    @SuppressWarnings("unused")
    private void defaultConfirmPayment () {
        log.info("default " +LocalDateTime.now());


    }
}
