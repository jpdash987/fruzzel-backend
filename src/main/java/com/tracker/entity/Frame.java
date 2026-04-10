package com.tracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Table(name = "frames")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Frame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Frame name is required")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Frame rate is required")
    @Positive(message = "Frame rate must be positive")
    @Column(nullable = false)
    private Double rate;
}
