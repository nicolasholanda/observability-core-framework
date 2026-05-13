package com.observability.core.service;

import com.observability.core.domain.enums.AggregationWindow;
import com.observability.core.domain.enums.AlertSeverity;
import com.observability.core.domain.enums.Percentile;
import com.observability.core.domain.model.AlertRule;
import com.observability.core.domain.model.ServiceEndpoint;
import com.observability.core.repository.AlertRuleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertRuleService {

    private final AlertRuleRepository repository;
    private final ServiceEndpointService serviceEndpointService;

    @Transactional
    public AlertRule create(Long serviceEndpointId, String name, Percentile percentile,
                            BigDecimal thresholdMs, AlertSeverity severity, AggregationWindow window) {
        ServiceEndpoint endpoint = serviceEndpointService.findById(serviceEndpointId);
        AlertRule rule = AlertRule.builder()
                .serviceEndpoint(endpoint)
                .name(name)
                .percentile(percentile)
                .thresholdMs(thresholdMs)
                .severity(severity)
                .window(window)
                .build();
        return repository.save(rule);
    }

    @Transactional(readOnly = true)
    public AlertRule findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AlertRule not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<AlertRule> findByEndpoint(Long serviceEndpointId) {
        return repository.findByServiceEndpointId(serviceEndpointId);
    }

    @Transactional
    public AlertRule disable(Long id) {
        AlertRule rule = findById(id);
        rule.setEnabled(false);
        return repository.save(rule);
    }

    @Transactional
    public AlertRule enable(Long id) {
        AlertRule rule = findById(id);
        rule.setEnabled(true);
        return repository.save(rule);
    }
}
