package com.shipping.booking.repository;

import com.shipping.booking.model.BookingSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;

@Repository
@RequiredArgsConstructor
public class BookingSequenceRepository {

    private static final String SEQUENCE_NAME = "booking_ref_sequence";
    private static final long INITIAL_VALUE = 1L;

    private final ReactiveMongoTemplate mongoTemplate;

    public Mono<Long> getNextSequence() {
        Query query = new Query(Criteria.where("id").is(SEQUENCE_NAME));
        Update update = new Update().inc("sequence_value", 1);
        
        return mongoTemplate.findAndModify(
                query,
                update,
                options().returnNew(true).upsert(true),
                BookingSequence.class
        ).map(BookingSequence::getSequenceValue)
         .defaultIfEmpty(INITIAL_VALUE);
    }
}