CREATE TABLE service_endpoints (
    id BIGSERIAL PRIMARY KEY,
    service_name VARCHAR(255) NOT NULL,
    endpoint_path VARCHAR(500) NOT NULL,
    http_method VARCHAR(10) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT uq_service_endpoint UNIQUE (service_name, endpoint_path, http_method)
);

CREATE INDEX idx_service_endpoints_service_name ON service_endpoints (service_name);
CREATE INDEX idx_service_endpoints_active ON service_endpoints (active);
