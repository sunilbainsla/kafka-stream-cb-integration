package com.sunilbainsla.kafkastreampoc.rest.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.vavr.CheckedRunnable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static com.sunilbainsla.kafkastreampoc.faulttolerance.config.CircuitBreakerInstances.CIRCUIT_BREAKER_INSTANCE_TOPIC_6;
import static com.sunilbainsla.kafkastreampoc.faulttolerance.config.CircuitBreakerInstances.CIRCUIT_BREAKER_INSTANCE_TOPIC_7;
import static com.sunilbainsla.kafkastreampoc.faulttolerance.config.CircuitBreakerInstances.CIRCUIT_BREAKER_INSTANCE_TOPIC_8;
import static com.sunilbainsla.kafkastreampoc.faulttolerance.config.RetryInstances.RETRY_INSTANCE_TOPIC_7;

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
    private static final String RATE_INSTANCE = "rate-instance";
    private static final String CB_INSTANCE5 = "cb-instance-topic5";
    private static final String RETRY_INSTANCE5 = "retry-instance-topic5";
    private static final String TIME_INSTANCE = "time-instance";

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
    @CircuitBreaker(name = CB_INSTANCE4)
    // @Retry(name = RETRY_INSTANCE4)
    // fallback will see if there is any error from rest client it will go to fallback and kafka will start to read the next steps
    // if fallback present it will also check if the rate limiter wont allow it be come to fallback and next offset will start
    //Sunil: I wont recomended it along with rate instance. timeoutDuration config wont work along with fallback as fallback will consume the offset

    // if timeoutDuration alone used it will generate exception if limit crossed and stop the consumer.
    // So it should not used just use other 2 properties and it would control the flow.
    //even with  fallback  circuit breaker wont work
    // if timeoutDuration will not set default will be 5s and in case  limitRefreshtime is >5 rate limiter generate exception. So better to use more than 5 s in timeout or
    // if u dont want to timeout put timeoutDuration: -1
    // @RateLimiter(name = RATE_INSTANCE )
    //    @RateLimiter(name = RATE_INSTANCE)
    //    public void restClient4(String message) {
    //        System.out.println("---------------------------------------------------------------------------------------------------->Calling form 3 for message= " + message);
    //        internalRestClient(message);
    //    }

    public CheckedRunnable restClient4(String message) {
        System.out.println("---------------------------------------------------------------------------------------------------->Calling form 3 for message= " + message);
        internalRestClient(message);
        return new CheckedRunnable() {
            @Override
            public void run() throws Throwable {
                System.out.println("from run method");

            }
        };
    }

    // With certain retry and circuit breaker
    @CircuitBreaker(name = CB_INSTANCE5)
    //@Retry(name = RETRY_INSTANCE5)
    //@TimeLimiter(name = TIME_INSTANCE,fallbackMethod ="defaultConfirmPayment" )
    public void restClient5(String message) {
        internalRestClient(message);
    }

    @CircuitBreaker(name = CIRCUIT_BREAKER_INSTANCE_TOPIC_6)
    public void restClient6(String message) {
        internalRestClient(message);
    }

    @CircuitBreaker(name = CIRCUIT_BREAKER_INSTANCE_TOPIC_7)
    @Retry(name = RETRY_INSTANCE_TOPIC_7)
    public void restClient7(String message) {
        internalRestClient(message);
    }

    @CircuitBreaker(name = CIRCUIT_BREAKER_INSTANCE_TOPIC_8)
    public void restClient8(String message) {
        internalRestClient(message);
    }

    public void internalRestClient(String message) {
        log.debug("rest call...");
        log.debug(" Making a request to {} at :{}", SERVICE_URL + message, LocalDateTime.now());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForObject(SERVICE_URL, String.class);
    }

    @SuppressWarnings("unused")
    private void defaultConfirmPayment(String message, Throwable t) {
        log.info("default --------------------------------message " + LocalDateTime.now());
    }
}
