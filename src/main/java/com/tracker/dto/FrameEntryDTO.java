package com.tracker.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FrameEntryDTO {

    private Long id;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Frame seller ID is required")
    private Long frameSellerId;

    private String frameSellerName;

    @NotNull(message = "Frame ID is required")
    private Long frameId;

    private String frameName;

    private Double frameRate;

    @NotNull(message = "Frame count is required")
    @Min(value = 0, message = "Frame count cannot be negative")
    private Integer frameCount;

    private Double mrp;
}
