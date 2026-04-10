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
public class FrameDTO {

    private Long id;

    @NotBlank(message = "Frame name is required")
    private String name;

    @NotNull(message = "Frame rate is required")
    @Positive(message = "Frame rate must be positive")
    private Double rate;
}
