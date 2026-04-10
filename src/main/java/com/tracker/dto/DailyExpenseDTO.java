package com.tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyExpenseDTO {

    private Long id;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotBlank(message = "Expense name is required")
    private String expenseName;

    @NotNull(message = "Amount is required")
    private Double amount;
}
