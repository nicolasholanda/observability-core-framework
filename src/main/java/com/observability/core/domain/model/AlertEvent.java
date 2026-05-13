package com.observability.core.domain.model;

import com.observability.core.domain.enums.AlertSeverity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "alert_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alert_rule_id", nullable = false)
    private AlertRule alertRule;

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal observedValueMs;

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal thresholdMs;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertSeverity severity;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false, updatable = false)
    private LocalDateTime triggeredAt;

    @PrePersist
    private void prePersist() {
        this.triggeredAt = LocalDateTime.now();
    }
}
