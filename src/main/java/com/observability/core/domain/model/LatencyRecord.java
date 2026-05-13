package com.observability.core.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "latency_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LatencyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_endpoint_id", nullable = false)
    private ServiceEndpoint serviceEndpoint;

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal latencyMs;

    @Column(nullable = false)
    private int statusCode;

    @Column
    private String traceId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime recordedAt;

    @PrePersist
    private void prePersist() {
        this.recordedAt = LocalDateTime.now();
    }
}
