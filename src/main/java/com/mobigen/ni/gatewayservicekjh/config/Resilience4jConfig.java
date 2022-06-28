package com.mobigen.ni.gatewayservicekjh.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;

@Slf4j
@Configuration
@RefreshScope
public class Resilience4jConfig {
    @Value("${resilience4j.circuitbreaker.custom.minimumNumberOfCalls:5}")
    private int customMinimumNumberOfCalls;
    @Value("${resilience4j.circuitbreaker.custom.failureRateThreshold:50}")
    private float customFailureRateThreshold;
    @Value("${resilience4j.circuitbreaker.custom.slidingWindowType:COUNT_BASED}")
    private String customSlidingWindowType;
    @Value("${resilience4j.circuitbreaker.custom.slidingWindowSize:10}")
    private int customSlidingWindowSize;
    @Value("${resilience4j.circuitbreaker.custom.waitDurationInOpenState:5000}")
    private long customWaitDurationInOpenState;
    @Value("${resilience4j.circuitbreaker.custom.slowCallDurationThreshold:3000}")
    private long customSlowCallDurationThreshold;
    @Value("${resilience4j.circuitbreaker.custom.slowCallRateThreshold:100}")
    private float customSlowCallRateThreshold;
    @Value("${resilience4j.timeout.custom:1000}")
    private long customTimeout;
    @Bean public Customizer<ReactiveResilience4JCircuitBreakerFactory> myCustomizer() {
        log.info(">>>>>>>>>>>> START myCustomizer()");
        CircuitBreakerConfig.SlidingWindowType winType = ("COUNT_BASED".equals(this.customSlidingWindowType)? CircuitBreakerConfig.SlidingWindowType.COUNT_BASED: CircuitBreakerConfig.SlidingWindowType.TIME_BASED);
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .slidingWindowType(winType)
                .slidingWindowSize(this.customSlidingWindowSize)
                .minimumNumberOfCalls(this.customMinimumNumberOfCalls)
                .failureRateThreshold(this.customFailureRateThreshold)
                .waitDurationInOpenState(Duration.ofMillis(this.customWaitDurationInOpenState))
                .recordExceptions(java.io.IOException.class, java.util.concurrent.TimeoutException.class, org.springframework.web.server.ResponseStatusException.class)
                .build();
        return factory -> factory.configure(builder -> builder.circuitBreakerConfig(config)
                .build(), "mycb");
    }
}