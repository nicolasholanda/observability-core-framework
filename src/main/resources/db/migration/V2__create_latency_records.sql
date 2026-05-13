CREATE TABLE latency_records (
    id BIGSERIAL PRIMARY KEY,
    service_endpoint_id BIGINT NOT NULL,
    latency_ms NUMERIC(19, 6) NOT NULL,
    status_code INT NOT NULL,
    trace_id VARCHAR(128),
    recorded_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_latency_records_endpoint FOREIGN KEY (service_endpoint_id)
        REFERENCES service_endpoints (id) ON DELETE CASCADE
);

CREATE INDEX idx_latency_records_endpoint_id ON latency_records (service_endpoint_id);
CREATE INDEX idx_latency_records_recorded_at ON latency_records (recorded_at);
CREATE INDEX idx_latency_records_endpoint_recorded ON latency_records (service_endpoint_id, recorded_at);
