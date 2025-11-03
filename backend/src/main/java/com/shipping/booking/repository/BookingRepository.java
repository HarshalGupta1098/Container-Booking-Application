package com.shipping.booking.repository;

import com.shipping.booking.model.Booking;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface BookingRepository extends ReactiveMongoRepository<Booking, String> {
    
    Mono<Booking> findByBookingRef(String bookingRef);
    
    Mono<Boolean> existsByBookingRef(String bookingRef);
}