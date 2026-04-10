package com.tracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "frame_entries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FrameEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Date is required")
    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "frame_seller_id", nullable = false)
    @JsonIgnore
    private FrameSeller frameSeller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "frame_id", nullable = false)
    @JsonIgnore
    private Frame frame;

    @NotNull
    @Min(value = 0, message = "Frame count cannot be negative")
    @Column(name = "frame_count")
    private Integer frameCount;

    @Column(nullable = false)
    private Double mrp;

    @PrePersist
    @PreUpdate
    public void calculateMrp() {
        if (this.frame != null && this.frameCount != null) {
            this.mrp = this.frame.getRate() * this.frameCount;
        } else {
            this.mrp = 0.0;
        }
    }
}
