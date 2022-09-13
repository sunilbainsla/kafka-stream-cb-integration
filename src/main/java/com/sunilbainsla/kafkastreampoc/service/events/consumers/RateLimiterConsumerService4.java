package com.sunilbainsla.kafkastreampoc.service.events.consumers;

import com.sunilbainsla.kafkastreampoc.model.kafka.Payment;
import com.sunilbainsla.kafkastreampoc.rest.client.PocRestClient;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.vavr.CheckedRunnable;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.function.Consumer;

@Service
@Slf4j
public class RateLimiterConsumerService4 {
    RateLimiterConfig config = RateLimiterConfig.custom()
            .limitRefreshPeriod(Duration.ofMillis(2000))
            .limitForPeriod(100)
            // .timeoutDuration(Duration.ofMillis(25))
            .build();

    RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.of(config);
    RateLimiter rateLimiterWithCustomConfig = rateLimiterRegistry
            .rateLimiter("client4", config);
    private final PocRestClient pocRestClient;

    public RateLimiterConsumerService4(PocRestClient pocRestClient) {
        this.pocRestClient = pocRestClient;
    }

    @Bean
    public Consumer<KStream<Object, Payment>> topic4Consumer() {
        return input -> input.foreach(this::businessLogic);
    }

    private void businessLogic(Object key, Payment val) {

        // Create registry

        log.info("Consumption start for the message : {}", val.getMessage());
        CheckedRunnable restrictedCall = RateLimiter
                .decorateCheckedRunnable(rateLimiterWithCustomConfig, pocRestClient.restClient4(val.getMessage()));
        Try.run(restrictedCall)
                .andThenTry(restrictedCall)
                .onFailure(throwable -> log.info("errrrrror" + throwable.getMessage()));
        // pocRestClient.restClient4(val.getMessage());
        log.debug("topic4Consumer end...");
    }
}
