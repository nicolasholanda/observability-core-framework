package com.observability.core.repository;

import com.observability.core.domain.model.AlertEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AlertEventRepository extends JpaRepository<AlertEvent, Long> {

    List<AlertEvent> findByAlertRuleId(Long alertRuleId);

    List<AlertEvent> findByAlertRuleIdAndTriggeredAtBetween(
            Long alertRuleId, LocalDateTime from, LocalDateTime to);

    List<AlertEvent> findByAlertRule_ServiceEndpoint_Id(Long serviceEndpointId);

    List<AlertEvent> findByAlertRule_ServiceEndpoint_IdAndTriggeredAtBetween(
            Long serviceEndpointId, LocalDateTime from, LocalDateTime to);
}
