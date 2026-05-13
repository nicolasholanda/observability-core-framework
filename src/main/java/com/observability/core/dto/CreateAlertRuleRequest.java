package com.observability.core.dto;

import com.observability.core.domain.enums.AggregationWindow;
import com.observability.core.domain.enums.AlertSeverity;
import com.observability.core.domain.enums.Percentile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateAlertRuleRequest {

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotNull
    private Percentile percentile;

    @NotNull
    @Positive
    private BigDecimal thresholdMs;

    @NotNull
    private AlertSeverity severity;

    @NotNull
    private AggregationWindow window;
}
