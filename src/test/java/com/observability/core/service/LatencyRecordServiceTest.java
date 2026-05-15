package com.observability.core.service;

import com.observability.core.domain.model.LatencyRecord;
import com.observability.core.domain.model.ServiceEndpoint;
import com.observability.core.repository.LatencyRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LatencyRecordServiceTest {

    @Mock
    private LatencyRecordRepository repository;

    @Mock
    private ServiceEndpointService serviceEndpointService;

    @Mock
    private AlertEvaluationService alertEvaluationService;

    @InjectMocks
    private LatencyRecordService service;

    @Test
    void record_shouldSaveAndTriggerEvaluation() {
        ServiceEndpoint endpoint = ServiceEndpoint.builder().id(1L).build();
        when(serviceEndpointService.findById(1L)).thenReturn(endpoint);

        LatencyRecord saved = LatencyRecord.builder().id(10L).serviceEndpoint(endpoint)
                .latencyMs(new BigDecimal("120.5")).statusCode(200).build();
        when(repository.save(any())).thenReturn(saved);

        LatencyRecord result = service.record(1L, new BigDecimal("120.5"), 200, "trace-1");

        assertThat(result.getId()).isEqualTo(10L);
        verify(alertEvaluationService).evaluateForEndpoint(1L);
    }

    @Test
    void findByEndpointAndTimeRange_shouldReturnRecords() {
        LocalDateTime from = LocalDateTime.now().minusHours(1);
        LocalDateTime to = LocalDateTime.now();
        List<LatencyRecord> records = List.of(LatencyRecord.builder().id(1L).build());
        when(repository.findByServiceEndpointIdAndRecordedAtBetween(1L, from, to)).thenReturn(records);

        assertThat(service.findByEndpointAndTimeRange(1L, from, to)).hasSize(1);
    }
}
