package com.observability.core.service;

import com.observability.core.domain.enums.AggregationWindow;
import com.observability.core.domain.enums.AlertSeverity;
import com.observability.core.domain.enums.Percentile;
import com.observability.core.domain.model.AlertRule;
import com.observability.core.domain.model.ServiceEndpoint;
import com.observability.core.exception.ResourceNotFoundException;
import com.observability.core.repository.AlertRuleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlertRuleServiceTest {

    @Mock
    private AlertRuleRepository repository;

    @Mock
    private ServiceEndpointService serviceEndpointService;

    @InjectMocks
    private AlertRuleService service;

    @Test
    void create_shouldSaveAndReturnRule() {
        ServiceEndpoint endpoint = ServiceEndpoint.builder().id(1L).build();
        when(serviceEndpointService.findById(1L)).thenReturn(endpoint);

        AlertRule saved = AlertRule.builder().id(5L).serviceEndpoint(endpoint)
                .name("High P95").percentile(Percentile.P95)
                .thresholdMs(new BigDecimal("300")).severity(AlertSeverity.WARNING)
                .window(AggregationWindow.FIVE_MINUTES).build();
        when(repository.save(any())).thenReturn(saved);

        AlertRule result = service.create(1L, "High P95", Percentile.P95,
                new BigDecimal("300"), AlertSeverity.WARNING, AggregationWindow.FIVE_MINUTES);

        assertThat(result.getId()).isEqualTo(5L);
        assertThat(result.getPercentile()).isEqualTo(Percentile.P95);
    }

    @Test
    void findById_shouldThrowResourceNotFoundException_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void disable_shouldSetEnabledFalse() {
        AlertRule rule = AlertRule.builder().id(1L).enabled(true).build();
        when(repository.findById(1L)).thenReturn(Optional.of(rule));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        AlertRule result = service.disable(1L);

        assertThat(result.isEnabled()).isFalse();
    }

    @Test
    void enable_shouldSetEnabledTrue() {
        AlertRule rule = AlertRule.builder().id(1L).enabled(false).build();
        when(repository.findById(1L)).thenReturn(Optional.of(rule));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        AlertRule result = service.enable(1L);

        assertThat(result.isEnabled()).isTrue();
    }
}
