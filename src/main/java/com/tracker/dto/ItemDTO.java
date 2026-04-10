package com.tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDTO {

    private Long id;

    @NotBlank(message = "Item name is required")
    private String name;

    @NotNull(message = "Default rate is required")
    @Positive(message = "Default rate must be positive")
    private Double defaultRate;

    @NotNull(message = "Default profit rate is required")
    @Positive(message = "Default profit rate must be positive")
    private Double defaultProfitRate;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    private String customerName;
}
