package com.observability.core.service;

import com.observability.core.domain.enums.AggregationWindow;
import com.observability.core.domain.enums.AlertSeverity;
import com.observability.core.domain.enums.Percentile;
import com.observability.core.domain.model.AlertEvent;
import com.observability.core.domain.model.AlertRule;
import com.observability.core.domain.model.ServiceEndpoint;
import com.observability.core.repository.AlertEventRepository;
import com.observability.core.repository.AlertRuleRepository;
import com.observability.core.repository.LatencyRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertEvaluationServiceTest {

    @Mock
    private AlertRuleRepository alertRuleRepository;

    @Mock
    private AlertEventRepository alertEventRepository;

    @Mock
    private LatencyRecordRepository latencyRecordRepository;

    @InjectMocks
    private AlertEvaluationService service;

    private AlertRule buildRule(BigDecimal thresholdMs) {
        ServiceEndpoint endpoint = ServiceEndpoint.builder().id(1L).build();
        return AlertRule.builder()
                .id(10L)
                .serviceEndpoint(endpoint)
                .name("P99 rule")
                .percentile(Percentile.P99)
                .thresholdMs(thresholdMs)
                .severity(AlertSeverity.CRITICAL)
                .window(AggregationWindow.FIVE_MINUTES)
                .enabled(true)
                .build();
    }

    @Test
    void evaluateForEndpoint_shouldCreateEvent_whenThresholdExceeded() {
        AlertRule rule = buildRule(new BigDecimal("200"));
        when(alertRuleRepository.findByServiceEndpointIdAndEnabledTrue(1L)).thenReturn(List.of(rule));
        List<BigDecimal> sorted = List.of(
                new BigDecimal("100"), new BigDecimal("150"), new BigDecimal("300"), new BigDecimal("500"));
        when(latencyRecordRepository.findLatencyValuesSorted(eq(1L), any())).thenReturn(sorted);

        service.evaluateForEndpoint(1L);

        ArgumentCaptor<AlertEvent> captor = ArgumentCaptor.forClass(AlertEvent.class);
        verify(alertEventRepository).save(captor.capture());
        assertThat(captor.getValue().getSeverity()).isEqualTo(AlertSeverity.CRITICAL);
        assertThat(captor.getValue().getObservedValueMs()).isGreaterThan(new BigDecimal("200"));
    }

    @Test
    void evaluateForEndpoint_shouldNotCreateEvent_whenThresholdNotExceeded() {
        AlertRule rule = buildRule(new BigDecimal("1000"));
        when(alertRuleRepository.findByServiceEndpointIdAndEnabledTrue(1L)).thenReturn(List.of(rule));
        List<BigDecimal> sorted = List.of(new BigDecimal("100"), new BigDecimal("200"), new BigDecimal("300"));
        when(latencyRecordRepository.findLatencyValuesSorted(eq(1L), any())).thenReturn(sorted);

        service.evaluateForEndpoint(1L);

        verify(alertEventRepository, never()).save(any());
    }

    @Test
    void evaluateForEndpoint_shouldSkip_whenNoData() {
        AlertRule rule = buildRule(new BigDecimal("200"));
        when(alertRuleRepository.findByServiceEndpointIdAndEnabledTrue(1L)).thenReturn(List.of(rule));
        when(latencyRecordRepository.findLatencyValuesSorted(eq(1L), any())).thenReturn(List.of());

        service.evaluateForEndpoint(1L);

        verify(alertEventRepository, never()).save(any());
    }
}
