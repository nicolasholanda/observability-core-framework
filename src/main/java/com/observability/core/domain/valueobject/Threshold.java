package com.observability.core.domain.valueobject;

import com.observability.core.domain.enums.AlertSeverity;

import java.math.BigDecimal;
import java.util.Objects;

public final class Threshold {

    private final BigDecimal valueMs;
    private final AlertSeverity severity;

    private Threshold(BigDecimal valueMs, AlertSeverity severity) {
        if (valueMs == null || valueMs.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Threshold must be positive");
        }
        if (severity == null) {
            throw new IllegalArgumentException("Severity must not be null");
        }
        this.valueMs = valueMs;
        this.severity = severity;
    }

    public static Threshold of(BigDecimal valueMs, AlertSeverity severity) {
        return new Threshold(valueMs, severity);
    }

    public boolean isExceededBy(LatencyDuration duration) {
        return duration.toMillis().compareTo(valueMs) > 0;
    }

    public BigDecimal getValueMs() {
        return valueMs;
    }

    public AlertSeverity getSeverity() {
        return severity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Threshold that)) return false;
        return Objects.equals(valueMs, that.valueMs) && severity == that.severity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(valueMs, severity);
    }

    @Override
    public String toString() {
        return severity + "@" + valueMs.toPlainString() + "ms";
    }
}
