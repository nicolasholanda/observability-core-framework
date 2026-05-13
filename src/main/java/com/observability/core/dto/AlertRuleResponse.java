package com.observability.core.dto;

import com.observability.core.domain.enums.AggregationWindow;
import com.observability.core.domain.enums.AlertSeverity;
import com.observability.core.domain.enums.Percentile;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class AlertRuleResponse {

    private Long id;
    private Long serviceEndpointId;
    private String name;
    private Percentile percentile;
    private BigDecimal thresholdMs;
    private AlertSeverity severity;
    private AggregationWindow window;
    private boolean enabled;
    private LocalDateTime createdAt;
}
