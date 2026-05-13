package com.observability.core.dto;

import com.observability.core.domain.enums.AlertSeverity;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class AlertEventResponse {

    private Long id;
    private Long alertRuleId;
    private BigDecimal observedValueMs;
    private BigDecimal thresholdMs;
    private AlertSeverity severity;
    private String message;
    private LocalDateTime triggeredAt;
}
