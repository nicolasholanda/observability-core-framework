package com.observability.core.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ServiceEndpointResponse {

    private Long id;
    private String serviceName;
    private String endpointPath;
    private String httpMethod;
    private boolean active;
    private LocalDateTime createdAt;
}
