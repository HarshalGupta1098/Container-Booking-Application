package com.shipping.booking.service;

import com.shipping.booking.dto.AvailabilityCheckRequest;
import com.shipping.booking.dto.AvailabilityCheckResponse;
import com.shipping.booking.dto.ExternalAvailabilityResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
@Slf4j
public class AvailabilityService {

    private final WebClient webClient;
    private final int maxRetries;

    public AvailabilityService(
            WebClient.Builder webClientBuilder,
            @Value("${external.availability-service.url:https://maersk.com/api/bookings/checkAvailable}") String availabilityServiceUrl,
            @Value("${external.availability-service.timeout:5000}") int timeout,
            @Value("${external.availability-service.max-retries:3}") int maxRetries) {
        
        this.maxRetries = maxRetries;
        
        this.webClient = webClientBuilder
                .baseUrl(availabilityServiceUrl)
                .build();
        
        log.info("AvailabilityService initialized with URL: {}", availabilityServiceUrl);
    }

    public Mono<AvailabilityCheckResponse> checkAvailability(AvailabilityCheckRequest request) {
        log.debug("Checking availability for: {}", request);

        return callExternalAvailabilityService(request)
                .retryWhen(Retry.backoff(maxRetries, Duration.ofMillis(500))
                        .maxBackoff(Duration.ofSeconds(2))
                        .filter(throwable -> throwable instanceof WebClientResponseException || 
                                           throwable instanceof java.net.ConnectException))
                .map(this::mapToAvailabilityResponse)
                .doOnNext(response -> log.debug("Availability check result: {}", response))
                .onErrorResume(ex -> {
                    log.error("Error checking availability, defaulting to unavailable", ex);
                    return Mono.just(AvailabilityCheckResponse.builder()
                            .available(false)
                            .build());
                });
    }

    private Mono<ExternalAvailabilityResponse> callExternalAvailabilityService(
            AvailabilityCheckRequest request) {
        
        return webClient
                .post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ExternalAvailabilityResponse.class)
                .timeout(Duration.ofSeconds(5))
                .doOnError(WebClientResponseException.class, ex -> 
                        log.error("External service error: status={}, body={}", 
                                ex.getStatusCode(), ex.getResponseBodyAsString()))
                .doOnError(ex -> log.error("Error calling external service", ex));
    }

    private AvailabilityCheckResponse mapToAvailabilityResponse(
            ExternalAvailabilityResponse externalResponse) {
        
        boolean available = externalResponse.getAvailableSpace() != null 
                && externalResponse.getAvailableSpace() > 0;
        
        return AvailabilityCheckResponse.builder()
                .available(available)
                .build();
    }
}