package com.tracker.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemEntryDTO {

    private Long id;

    @NotNull(message = "Item ID is required")
    private Long itemId;

    private String itemName;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer qty;

    @NotNull(message = "Return quantity is required")
    @Min(value = 0, message = "Return quantity cannot be negative")
    private Integer returnQty;

    private Double rate;

    private Double profitRate;

    @NotNull(message = "Online amount is required")
    @Min(value = 0, message = "Online amount cannot be negative")
    private Double online;

    // Calculated fields (read-only in response)
    private Integer finalQty;
    private Double price;
    private Double profit;
}
