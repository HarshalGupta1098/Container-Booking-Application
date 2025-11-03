package com.shipping.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "booking_sequences")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSequence {

    @Id
    private String id;

    @Field("sequence_value")
    private Long sequenceValue;
}