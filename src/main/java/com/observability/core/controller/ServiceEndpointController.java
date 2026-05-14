package com.observability.core.controller;

import com.observability.core.dto.CreateServiceEndpointRequest;
import com.observability.core.dto.ServiceEndpointResponse;
import com.observability.core.mapper.ServiceEndpointMapper;
import com.observability.core.service.ServiceEndpointService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/endpoints")
@RequiredArgsConstructor
public class ServiceEndpointController {

    private final ServiceEndpointService service;
    private final ServiceEndpointMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceEndpointResponse create(@Valid @RequestBody CreateServiceEndpointRequest request) {
        return mapper.toResponse(service.create(request.getServiceName(), request.getEndpointPath(), request.getHttpMethod()));
    }

    @GetMapping("/{id}")
    public ServiceEndpointResponse findById(@PathVariable Long id) {
        return mapper.toResponse(service.findById(id));
    }

    @GetMapping
    public List<ServiceEndpointResponse> findAll() {
        return service.findAll().stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/by-service/{serviceName}")
    public List<ServiceEndpointResponse> findByServiceName(@PathVariable String serviceName) {
        return service.findByServiceName(serviceName).stream().map(mapper::toResponse).toList();
    }

    @GetMapping("/active")
    public List<ServiceEndpointResponse> findActive() {
        return service.findActive().stream().map(mapper::toResponse).toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable Long id) {
        service.deactivate(id);
    }
}
