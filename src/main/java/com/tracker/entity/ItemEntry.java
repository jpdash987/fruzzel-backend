package com.tracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "item_entries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_entry_id", nullable = false)
    @JsonIgnore
    private DailyEntry dailyEntry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    @JsonIgnore
    private Item item;

    @NotNull
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer qty;

    @NotNull
    @Min(value = 0, message = "Return quantity cannot be negative")
    @Column(name = "return_qty")
    private Integer returnQty;

    @NotNull
    private Double rate;

    @NotNull
    @Column(name = "profit_rate")
    private Double profitRate;

    @NotNull
    @Min(value = 0, message = "Online amount cannot be negative")
    private Double online;

    @Column(name = "final_qty")
    private Integer finalQty;

    private Double price;

    private Double profit;

    /**
     * Calculate derived fields before persisting.
     */
    @PrePersist
    @PreUpdate
    public void calculateFields() {
        this.finalQty = (this.qty != null && this.returnQty != null)
                ? this.qty - this.returnQty : 0;
        this.price = (this.finalQty != null && this.rate != null)
                ? this.finalQty * this.rate : 0.0;
        this.profit = (this.finalQty != null && this.profitRate != null)
                ? this.finalQty * this.profitRate : 0.0;
    }
}
