package com.tracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Table(name = "items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Item name is required")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Default rate is required")
    @Positive(message = "Default rate must be positive")
    @Column(name = "default_rate", nullable = false)
    private Double defaultRate;

    @NotNull(message = "Default profit rate is required")
    @Positive(message = "Default profit rate must be positive")
    @Column(name = "default_profit_rate", nullable = false)
    private Double defaultProfitRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;
}
