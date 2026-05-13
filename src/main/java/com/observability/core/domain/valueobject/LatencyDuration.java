package com.observability.core.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class LatencyDuration {

    private static final BigDecimal MICROS_PER_MS = new BigDecimal("1000");
    private static final BigDecimal NANOS_PER_MS = new BigDecimal("1000000");

    private final BigDecimal valueMs;

    private LatencyDuration(BigDecimal valueMs) {
        if (valueMs == null || valueMs.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Latency duration must be non-negative");
        }
        this.valueMs = valueMs.setScale(6, RoundingMode.HALF_UP);
    }

    public static LatencyDuration ofMillis(BigDecimal millis) {
        return new LatencyDuration(millis);
    }

    public static LatencyDuration ofMillis(double millis) {
        return new LatencyDuration(BigDecimal.valueOf(millis));
    }

    public static LatencyDuration ofMicros(BigDecimal micros) {
        return new LatencyDuration(micros.divide(MICROS_PER_MS, 6, RoundingMode.HALF_UP));
    }

    public static LatencyDuration ofNanos(long nanos) {
        return new LatencyDuration(BigDecimal.valueOf(nanos).divide(NANOS_PER_MS, 6, RoundingMode.HALF_UP));
    }

    public BigDecimal toMillis() {
        return valueMs;
    }

    public BigDecimal toMicros() {
        return valueMs.multiply(MICROS_PER_MS).setScale(3, RoundingMode.HALF_UP);
    }

    public boolean isGreaterThan(LatencyDuration other) {
        return this.valueMs.compareTo(other.valueMs) > 0;
    }

    public boolean isLessThan(LatencyDuration other) {
        return this.valueMs.compareTo(other.valueMs) < 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LatencyDuration that)) return false;
        return Objects.equals(valueMs, that.valueMs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valueMs);
    }

    @Override
    public String toString() {
        return valueMs.toPlainString() + "ms";
    }
}
