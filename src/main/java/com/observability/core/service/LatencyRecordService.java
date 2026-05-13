package com.observability.core.service;

import com.observability.core.domain.model.LatencyRecord;
import com.observability.core.domain.model.ServiceEndpoint;
import com.observability.core.repository.LatencyRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LatencyRecordService {

    private final LatencyRecordRepository repository;
    private final ServiceEndpointService serviceEndpointService;
    private final AlertEvaluationService alertEvaluationService;

    @Transactional
    public LatencyRecord record(Long serviceEndpointId, BigDecimal latencyMs, int statusCode, String traceId) {
        ServiceEndpoint endpoint = serviceEndpointService.findById(serviceEndpointId);
        LatencyRecord record = LatencyRecord.builder()
                .serviceEndpoint(endpoint)
                .latencyMs(latencyMs)
                .statusCode(statusCode)
                .traceId(traceId)
                .build();
        LatencyRecord saved = repository.save(record);
        alertEvaluationService.evaluateForEndpoint(serviceEndpointId);
        return saved;
    }

    @Transactional(readOnly = true)
    public List<LatencyRecord> findByEndpointAndTimeRange(Long serviceEndpointId, LocalDateTime from, LocalDateTime to) {
        return repository.findByServiceEndpointIdAndRecordedAtBetween(serviceEndpointId, from, to);
    }
}
