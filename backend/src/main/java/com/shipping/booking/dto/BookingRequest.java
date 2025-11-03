package com.shipping.booking.dto;

import com.shipping.booking.model.enums.ContainerType;
import com.shipping.booking.validation.ContainerSizeConstraint;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to create a new booking")
@Getter
@Setter
public class BookingRequest {

    @NotNull(message = "Container type is required")
    @Schema(description = "Type of container", example = "DRY")
    private ContainerType containerType;

    @NotNull(message = "Container size is required")
    @ContainerSizeConstraint
    @Schema(description = "Size of container in feet", example = "20")
    private Integer containerSize;

    @NotBlank(message = "Origin is required")
    @Size(min = 5, max = 20, message = "Origin must be between 5 and 20 characters")
    @Schema(description = "Port of origin", example = "Southampton")
    private String origin;

    @NotBlank(message = "Destination is required")
    @Size(min = 5, max = 20, message = "Destination must be between 5 and 20 characters")
    @Schema(description = "Destination port", example = "Singapore")
    private String destination;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 100, message = "Quantity must not exceed 100")
    @Schema(description = "Number of containers to book", example = "5")
    private Integer quantity;

    @NotBlank(message = "Timestamp is required")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z$",
            message = "Timestamp must be in ISO-8601 UTC format")
    @Schema(description = "Booking timestamp", example = "2020-10-12T13:53:09Z")
    private String timestamp;
}
