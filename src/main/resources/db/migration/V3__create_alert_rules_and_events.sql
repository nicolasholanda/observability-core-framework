CREATE TABLE alert_rules (
    id BIGSERIAL PRIMARY KEY,
    service_endpoint_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    percentile VARCHAR(10) NOT NULL,
    threshold_ms NUMERIC(19, 6) NOT NULL,
    severity VARCHAR(20) NOT NULL,
    window VARCHAR(20) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_alert_rules_endpoint FOREIGN KEY (service_endpoint_id)
        REFERENCES service_endpoints (id) ON DELETE CASCADE
);

CREATE INDEX idx_alert_rules_endpoint_id ON alert_rules (service_endpoint_id);
CREATE INDEX idx_alert_rules_enabled ON alert_rules (enabled);

CREATE TABLE alert_events (
    id BIGSERIAL PRIMARY KEY,
    alert_rule_id BIGINT NOT NULL,
    observed_value_ms NUMERIC(19, 6) NOT NULL,
    threshold_ms NUMERIC(19, 6) NOT NULL,
    severity VARCHAR(20) NOT NULL,
    message TEXT NOT NULL,
    triggered_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_alert_events_rule FOREIGN KEY (alert_rule_id)
        REFERENCES alert_rules (id) ON DELETE CASCADE
);

CREATE INDEX idx_alert_events_rule_id ON alert_events (alert_rule_id);
CREATE INDEX idx_alert_events_triggered_at ON alert_events (triggered_at);
CREATE INDEX idx_alert_events_severity ON alert_events (severity);
