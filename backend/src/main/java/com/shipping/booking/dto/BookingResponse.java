package com.shipping.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response containing booking reference")
public class BookingResponse {

    @Schema(description = "Unique booking reference number", example = "957000001")
    private String bookingRef;
}