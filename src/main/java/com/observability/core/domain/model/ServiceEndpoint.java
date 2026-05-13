package com.observability.core.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_endpoints")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceEndpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String serviceName;

    @Column(nullable = false)
    private String endpointPath;

    @Column(nullable = false)
    private String httpMethod;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "serviceEndpoint", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<LatencyRecord> latencyRecords = new ArrayList<>();

    @OneToMany(mappedBy = "serviceEndpoint", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AlertRule> alertRules = new ArrayList<>();

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.active = true;
    }
}
