package com.tracker.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyEntryDTO {

    private Long id;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    private String customerName;

    @Valid
    private List<ItemEntryDTO> itemEntries;

    // Summary fields
    private Double totalPrice;
    private Double totalProfit;
    private Double totalOnline;
}
