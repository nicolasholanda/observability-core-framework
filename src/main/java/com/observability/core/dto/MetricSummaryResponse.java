package com.observability.core.dto;

import com.observability.core.domain.enums.AggregationWindow;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class MetricSummaryResponse {

    private Long serviceEndpointId;
    private String serviceName;
    private String endpointPath;
    private AggregationWindow window;
    private BigDecimal averageMs;
    private BigDecimal minMs;
    private BigDecimal maxMs;
    private Map<String, BigDecimal> percentiles;
    private long sampleCount;
    private LocalDateTime computedAt;
}
