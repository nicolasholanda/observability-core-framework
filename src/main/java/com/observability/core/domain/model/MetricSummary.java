package com.observability.core.domain.model;

import com.observability.core.domain.enums.AggregationWindow;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
public class MetricSummary {

    private final Long serviceEndpointId;
    private final String serviceName;
    private final String endpointPath;
    private final AggregationWindow window;
    private final BigDecimal averageMs;
    private final BigDecimal minMs;
    private final BigDecimal maxMs;
    private final Map<String, BigDecimal> percentiles;
    private final long sampleCount;
    private final LocalDateTime computedAt;
}
