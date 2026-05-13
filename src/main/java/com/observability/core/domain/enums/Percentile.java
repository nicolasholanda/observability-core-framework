package com.observability.core.domain.enums;

import java.math.BigDecimal;

public enum Percentile {
    P50(new BigDecimal("0.50")),
    P75(new BigDecimal("0.75")),
    P90(new BigDecimal("0.90")),
    P95(new BigDecimal("0.95")),
    P99(new BigDecimal("0.99")),
    P999(new BigDecimal("0.999"));

    private final BigDecimal value;

    Percentile(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }
}
