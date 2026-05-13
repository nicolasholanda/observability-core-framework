package com.observability.core.service;

import com.observability.core.domain.enums.AggregationWindow;
import com.observability.core.domain.enums.Percentile;
import com.observability.core.domain.model.MetricSummary;
import com.observability.core.domain.model.ServiceEndpoint;
import com.observability.core.repository.LatencyRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MetricSummaryService {

    private final LatencyRecordRepository latencyRecordRepository;
    private final ServiceEndpointService serviceEndpointService;

    @Transactional(readOnly = true)
    public MetricSummary computeSummary(Long serviceEndpointId, AggregationWindow window) {
        ServiceEndpoint endpoint = serviceEndpointService.findById(serviceEndpointId);
        LocalDateTime from = LocalDateTime.now().minus(window.getDuration());

        List<BigDecimal> sorted = latencyRecordRepository.findLatencyValuesSorted(serviceEndpointId, from);
        BigDecimal avg = latencyRecordRepository.findAverageLatency(serviceEndpointId, from).orElse(BigDecimal.ZERO);
        BigDecimal min = latencyRecordRepository.findMinLatency(serviceEndpointId, from).orElse(BigDecimal.ZERO);
        BigDecimal max = latencyRecordRepository.findMaxLatency(serviceEndpointId, from).orElse(BigDecimal.ZERO);
        long count = latencyRecordRepository.countByServiceEndpointIdAndFrom(serviceEndpointId, from);

        Map<String, BigDecimal> percentiles = new LinkedHashMap<>();
        for (Percentile p : Percentile.values()) {
            percentiles.put(p.name(), computePercentile(sorted, p.getValue().doubleValue()));
        }

        return MetricSummary.builder()
                .serviceEndpointId(serviceEndpointId)
                .serviceName(endpoint.getServiceName())
                .endpointPath(endpoint.getEndpointPath())
                .window(window)
                .averageMs(avg)
                .minMs(min)
                .maxMs(max)
                .percentiles(percentiles)
                .sampleCount(count)
                .computedAt(LocalDateTime.now())
                .build();
    }

    private BigDecimal computePercentile(List<BigDecimal> sorted, double percentile) {
        if (sorted.isEmpty()) return BigDecimal.ZERO;
        int index = (int) Math.floor(percentile * (sorted.size() - 1));
        return sorted.get(Math.min(index, sorted.size() - 1));
    }
}
