package com.observability.core.repository;

import com.observability.core.domain.model.AlertRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRuleRepository extends JpaRepository<AlertRule, Long> {

    List<AlertRule> findByServiceEndpointId(Long serviceEndpointId);

    List<AlertRule> findByServiceEndpointIdAndEnabledTrue(Long serviceEndpointId);

    List<AlertRule> findByEnabledTrue();
}
