package com.observability.core.service;

import com.observability.core.domain.model.AlertEvent;
import com.observability.core.domain.model.AlertRule;
import com.observability.core.repository.AlertEventRepository;
import com.observability.core.repository.AlertRuleRepository;
import com.observability.core.repository.LatencyRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertEvaluationService {

    private final AlertRuleRepository alertRuleRepository;
    private final AlertEventRepository alertEventRepository;
    private final LatencyRecordRepository latencyRecordRepository;

    @Transactional
    public void evaluateForEndpoint(Long serviceEndpointId) {
        List<AlertRule> rules = alertRuleRepository.findByServiceEndpointIdAndEnabledTrue(serviceEndpointId);
        for (AlertRule rule : rules) {
            LocalDateTime from = LocalDateTime.now().minus(rule.getWindow().getDuration());
            List<BigDecimal> sorted = latencyRecordRepository.findLatencyValuesSorted(serviceEndpointId, from);
            if (sorted.isEmpty()) continue;

            BigDecimal observed = computePercentile(sorted, rule.getPercentile().getValue().doubleValue());
            if (observed.compareTo(rule.getThresholdMs()) > 0) {
                AlertEvent event = AlertEvent.builder()
                        .alertRule(rule)
                        .observedValueMs(observed)
                        .thresholdMs(rule.getThresholdMs())
                        .severity(rule.getSeverity())
                        .message(String.format("%s exceeded: %sms > %sms",
                                rule.getPercentile().name(), observed.toPlainString(), rule.getThresholdMs().toPlainString()))
                        .build();
                alertEventRepository.save(event);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<AlertEvent> findEventsByEndpoint(Long serviceEndpointId, LocalDateTime from, LocalDateTime to) {
        return alertEventRepository.findByAlertRule_ServiceEndpoint_IdAndTriggeredAtBetween(serviceEndpointId, from, to);
    }

    @Transactional(readOnly = true)
    public List<AlertEvent> findEventsByRule(Long alertRuleId, LocalDateTime from, LocalDateTime to) {
        return alertEventRepository.findByAlertRuleIdAndTriggeredAtBetween(alertRuleId, from, to);
    }

    private BigDecimal computePercentile(List<BigDecimal> sorted, double percentile) {
        int index = (int) Math.floor(percentile * (sorted.size() - 1));
        return sorted.get(Math.min(index, sorted.size() - 1));
    }
}
