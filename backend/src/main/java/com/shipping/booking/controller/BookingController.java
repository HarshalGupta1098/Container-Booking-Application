package com.shipping.booking.controller;

import com.shipping.booking.dto.AvailabilityCheckRequest;
import com.shipping.booking.dto.AvailabilityCheckResponse;
import com.shipping.booking.dto.BookingRequest;
import com.shipping.booking.dto.BookingResponse;
import com.shipping.booking.service.AvailabilityService;
import com.shipping.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Container Booking API", description = "Endpoints for checking availability and creating bookings")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BookingController {

    private final AvailabilityService availabilityService;
    private final BookingService bookingService;

    @PostMapping(
            value = "/check-availability",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Check container availability",
            description = "Checks if containers are available at the yard"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Availability check completed",
                    content = @Content(schema = @Schema(implementation = AvailabilityCheckResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<AvailabilityCheckResponse> checkAvailability(
            @Valid @RequestBody AvailabilityCheckRequest request) {
        
        log.info("Availability check: type={}, size={}, qty={}", 
                request.getContainerType(), request.getContainerSize(), request.getQuantity());

        return availabilityService.checkAvailability(request)
                .doOnSuccess(response -> log.info("Available: {}", response.isAvailable()))
                .doOnError(error -> log.error("Error checking availability", error));
    }

    @PostMapping(
            value = "/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new booking",
            description = "Creates a new container booking"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Booking created",
                    content = @Content(schema = @Schema(implementation = BookingResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Mono<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request) {
        
        log.info("Creating booking: type={}, size={}, qty={}", 
                request.getContainerType(), request.getContainerSize(), request.getQuantity());

        return bookingService.createBooking(request)
                .doOnSuccess(response -> log.info("Booking created: {}", response.getBookingRef()))
                .doOnError(error -> log.error("Error creating booking", error));
    }
}
