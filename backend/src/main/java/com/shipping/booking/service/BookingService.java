package com.shipping.booking.service;

import com.shipping.booking.dto.BookingRequest;
import com.shipping.booking.dto.BookingResponse;
import com.shipping.booking.exception.BookingCreationException;
import com.shipping.booking.model.Booking;
import com.shipping.booking.repository.BookingRepository;
import com.shipping.booking.repository.BookingSequenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private static final String BOOKING_REF_PREFIX = "957";
    private static final int SEQUENCE_PADDING = 6;

    private final BookingRepository bookingRepository;
    private final BookingSequenceRepository sequenceRepository;

    public Mono<BookingResponse> createBooking(BookingRequest request) {
        return sequenceRepository.getNextSequence()
                .flatMap(sequence -> {
                    String bookingRef = generateBookingReference(sequence);
                    Booking booking = buildBooking(request, bookingRef);
                    
                    return bookingRepository.save(booking)
                            .map(savedBooking -> BookingResponse.builder()
                                    .bookingRef(savedBooking.getBookingRef())
                                    .build())
                            .doOnSuccess(response -> 
                                    log.debug("Booking saved successfully: {}", bookingRef))
                            .onErrorMap(this::handleDatabaseException);
                })
                .onErrorMap(ex -> {
                    if (ex instanceof BookingCreationException) {
                        return ex;
                    }
                    log.error("Unexpected error creating booking", ex);
                    return new BookingCreationException(
                            "Sorry there was a problem processing your request");
                });
    }

    private String generateBookingReference(Long sequence) {
        String sequenceStr = String.format("%0" + SEQUENCE_PADDING + "d", sequence);
        return BOOKING_REF_PREFIX + sequenceStr;
    }

    private Booking buildBooking(BookingRequest request, String bookingRef) {
        return Booking.builder()
                .bookingRef(bookingRef)
                .containerType(request.getContainerType())
                .containerSize(request.getContainerSize())
                .origin(request.getOrigin())
                .destination(request.getDestination())
                .quantity(request.getQuantity())
                .timestamp(Instant.parse(request.getTimestamp()))
                .createdAt(Instant.now())
                .build();
    }

    private Throwable handleDatabaseException(Throwable ex) {
        log.error("Database error while creating booking", ex);
        return new BookingCreationException(
                "Sorry there was a problem processing your request");
    }
}