package store.api;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.vavr.control.Try;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.function.Supplier;


@Component
public class StoreCircuitBreaker {

    public static final String STORE_CIRCUIT_BREAKER = "storeCircuitBreaker";
    private final CircuitBreaker circuitBreaker;

    public StoreCircuitBreaker() {

        var circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(2)
                .waitDurationInOpenState(Duration.ofMillis(10000))
                .permittedNumberOfCallsInHalfOpenState(2)
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(3)
                .build();

        var circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);
        circuitBreaker = circuitBreakerRegistry.circuitBreaker(STORE_CIRCUIT_BREAKER, circuitBreakerConfig);
    }



    public <T> T executeRequest(Supplier<T> request, Supplier<T> defaultValue) {
        var result = Try.ofSupplier(() -> circuitBreaker.executeSupplier(request))
                .recover(throwable -> {
                    System.out.println("Execution failed: " + throwable);
                    return defaultValue.get();
                });

        return result.get();
    }

}
