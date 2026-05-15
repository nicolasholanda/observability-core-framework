package com.observability.core.service;

import com.observability.core.domain.enums.AggregationWindow;
import com.observability.core.domain.model.MetricSummary;
import com.observability.core.domain.model.ServiceEndpoint;
import com.observability.core.repository.LatencyRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MetricSummaryServiceTest {

    @Mock
    private LatencyRecordRepository latencyRecordRepository;

    @Mock
    private ServiceEndpointService serviceEndpointService;

    @InjectMocks
    private MetricSummaryService service;

    @Test
    void computeSummary_shouldReturnCorrectMetrics() {
        ServiceEndpoint endpoint = ServiceEndpoint.builder()
                .id(1L).serviceName("svc").endpointPath("/api").build();
        when(serviceEndpointService.findById(1L)).thenReturn(endpoint);

        List<BigDecimal> sorted = List.of(
                new BigDecimal("100"), new BigDecimal("150"),
                new BigDecimal("200"), new BigDecimal("300"), new BigDecimal("500"));
        when(latencyRecordRepository.findLatencyValuesSorted(eq(1L), any())).thenReturn(sorted);
        when(latencyRecordRepository.findAverageLatency(eq(1L), any())).thenReturn(Optional.of(new BigDecimal("250")));
        when(latencyRecordRepository.findMinLatency(eq(1L), any())).thenReturn(Optional.of(new BigDecimal("100")));
        when(latencyRecordRepository.findMaxLatency(eq(1L), any())).thenReturn(Optional.of(new BigDecimal("500")));
        when(latencyRecordRepository.countByServiceEndpointIdAndFrom(eq(1L), any())).thenReturn(5L);

        MetricSummary summary = service.computeSummary(1L, AggregationWindow.FIVE_MINUTES);

        assertThat(summary.getSampleCount()).isEqualTo(5L);
        assertThat(summary.getAverageMs()).isEqualByComparingTo(new BigDecimal("250"));
        assertThat(summary.getMinMs()).isEqualByComparingTo(new BigDecimal("100"));
        assertThat(summary.getMaxMs()).isEqualByComparingTo(new BigDecimal("500"));
        assertThat(summary.getPercentiles()).containsKey("P99");
        assertThat(summary.getWindow()).isEqualTo(AggregationWindow.FIVE_MINUTES);
    }

    @Test
    void computeSummary_shouldReturnZeros_whenNoData() {
        ServiceEndpoint endpoint = ServiceEndpoint.builder().id(1L).serviceName("svc").endpointPath("/api").build();
        when(serviceEndpointService.findById(1L)).thenReturn(endpoint);
        when(latencyRecordRepository.findLatencyValuesSorted(eq(1L), any())).thenReturn(List.of());
        when(latencyRecordRepository.findAverageLatency(eq(1L), any())).thenReturn(Optional.empty());
        when(latencyRecordRepository.findMinLatency(eq(1L), any())).thenReturn(Optional.empty());
        when(latencyRecordRepository.findMaxLatency(eq(1L), any())).thenReturn(Optional.empty());
        when(latencyRecordRepository.countByServiceEndpointIdAndFrom(eq(1L), any())).thenReturn(0L);

        MetricSummary summary = service.computeSummary(1L, AggregationWindow.ONE_HOUR);

        assertThat(summary.getSampleCount()).isZero();
        assertThat(summary.getAverageMs()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(summary.getPercentiles()).allSatisfy((k, v) -> assertThat(v).isEqualByComparingTo(BigDecimal.ZERO));
    }
}
