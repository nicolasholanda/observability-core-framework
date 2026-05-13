package com.observability.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateServiceEndpointRequest {

    @NotBlank
    @Size(max = 255)
    private String serviceName;

    @NotBlank
    @Size(max = 500)
    private String endpointPath;

    @NotBlank
    @Pattern(regexp = "GET|POST|PUT|PATCH|DELETE|HEAD|OPTIONS", message = "must be a valid HTTP method")
    private String httpMethod;
}
