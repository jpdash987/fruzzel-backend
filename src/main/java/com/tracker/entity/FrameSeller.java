package com.tracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "frame_sellers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FrameSeller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Frame seller name is required")
    @Column(nullable = false)
    private String name;
}
