package com.sunilbainsla.kafkastreampoc.faulttolerance.config;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
public class RateLimiterInstances {
    //    RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.ofDefaults();

    RateLimiterConfig config = RateLimiterConfig.custom()
            .limitRefreshPeriod(Duration.ofMillis(1))
            .limitForPeriod(10)
            .timeoutDuration(Duration.ofMillis(25))
            .build();

    // Create registry
    RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.of(config);

    // Use registry
    RateLimiter rateLimiterWithDefaultConfig = rateLimiterRegistry
            .rateLimiter("name1");

    RateLimiter rateLimiterWithCustomConfig = rateLimiterRegistry
            .rateLimiter("name2", config);
    // Decorate your call to BackendService.doSomething()
    //    CheckedRunnable restrictedCall = RateLimiter
    //            .decorateCheckedRunnable(rateLimiterWithCustomConfig, backendService::doSomething);

    //Try.run(restrictedCall)
    //            .
    //
    //    andThenTry(restrictedCall)
    //    .
    //
    //    onFailure((RequestNotPermitted throwable) ->System.out.println("Wait before call it again :)"));
}
