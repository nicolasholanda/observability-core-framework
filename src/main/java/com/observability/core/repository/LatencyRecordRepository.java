package com.observability.core.repository;

import com.observability.core.domain.model.LatencyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LatencyRecordRepository extends JpaRepository<LatencyRecord, Long> {

    List<LatencyRecord> findByServiceEndpointIdAndRecordedAtBetween(
            Long serviceEndpointId, LocalDateTime from, LocalDateTime to);

    @Query("SELECT AVG(r.latencyMs) FROM LatencyRecord r WHERE r.serviceEndpoint.id = :endpointId AND r.recordedAt >= :from")
    Optional<BigDecimal> findAverageLatency(@Param("endpointId") Long endpointId, @Param("from") LocalDateTime from);

    @Query("SELECT MIN(r.latencyMs) FROM LatencyRecord r WHERE r.serviceEndpoint.id = :endpointId AND r.recordedAt >= :from")
    Optional<BigDecimal> findMinLatency(@Param("endpointId") Long endpointId, @Param("from") LocalDateTime from);

    @Query("SELECT MAX(r.latencyMs) FROM LatencyRecord r WHERE r.serviceEndpoint.id = :endpointId AND r.recordedAt >= :from")
    Optional<BigDecimal> findMaxLatency(@Param("endpointId") Long endpointId, @Param("from") LocalDateTime from);

    @Query("SELECT COUNT(r) FROM LatencyRecord r WHERE r.serviceEndpoint.id = :endpointId AND r.recordedAt >= :from")
    long countByServiceEndpointIdAndFrom(@Param("endpointId") Long endpointId, @Param("from") LocalDateTime from);

    @Query("SELECT r.latencyMs FROM LatencyRecord r WHERE r.serviceEndpoint.id = :endpointId AND r.recordedAt >= :from ORDER BY r.latencyMs ASC")
    List<BigDecimal> findLatencyValuesSorted(@Param("endpointId") Long endpointId, @Param("from") LocalDateTime from);
}
