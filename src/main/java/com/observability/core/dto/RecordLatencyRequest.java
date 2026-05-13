package com.observability.core.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RecordLatencyRequest {

    @NotNull
    @Positive
    private BigDecimal latencyMs;

    @Min(100)
    @Max(599)
    private int statusCode;

    private String traceId;
}
