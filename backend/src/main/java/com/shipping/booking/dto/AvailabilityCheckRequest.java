package com.shipping.booking.dto;

import com.shipping.booking.model.enums.ContainerType;
import com.shipping.booking.validation.ContainerSizeConstraint;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to check container availability")
public class AvailabilityCheckRequest {

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
    @Schema(description = "Number of containers needed", example = "5")
    private Integer quantity;
}