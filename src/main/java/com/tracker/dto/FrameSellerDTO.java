package com.tracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FrameSellerDTO {

    private Long id;

    @NotBlank(message = "Frame seller name is required")
    private String name;
}
