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
@Schema(description = "Response indicating container availability")
public class AvailabilityCheckResponse {

    @Schema(description = "Whether containers are available", example = "true")
    private boolean available;
}
