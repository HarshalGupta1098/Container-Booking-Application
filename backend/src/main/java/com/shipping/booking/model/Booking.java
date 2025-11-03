package com.shipping.booking.model;

import com.shipping.booking.model.enums.ContainerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "bookings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @Field("booking_ref")
    private String bookingRef;

    @Field("container_type")
    private ContainerType containerType;

    @Field("container_size")
    private Integer containerSize;

    @Field("origin")
    private String origin;

    @Field("destination")
    private String destination;

    @Field("quantity")
    private Integer quantity;

    @Field("timestamp")
    private Instant timestamp;

    @CreatedDate
    @Field("created_at")
    private Instant createdAt;
}