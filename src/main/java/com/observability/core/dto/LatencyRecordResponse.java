package com.observability.core.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class LatencyRecordResponse {

    private Long id;
    private Long serviceEndpointId;
    private BigDecimal latencyMs;
    private int statusCode;
    private String traceId;
    private LocalDateTime recordedAt;
}
