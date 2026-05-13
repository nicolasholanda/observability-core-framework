package com.observability.core.service;

import com.observability.core.domain.model.ServiceEndpoint;
import com.observability.core.repository.ServiceEndpointRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceEndpointService {

    private final ServiceEndpointRepository repository;

    @Transactional
    public ServiceEndpoint create(String serviceName, String endpointPath, String httpMethod) {
        if (repository.existsByServiceNameAndEndpointPathAndHttpMethod(serviceName, endpointPath, httpMethod)) {
            throw new IllegalArgumentException("Endpoint already registered: " + httpMethod + " " + serviceName + endpointPath);
        }
        ServiceEndpoint endpoint = ServiceEndpoint.builder()
                .serviceName(serviceName)
                .endpointPath(endpointPath)
                .httpMethod(httpMethod.toUpperCase())
                .build();
        return repository.save(endpoint);
    }

    @Transactional(readOnly = true)
    public ServiceEndpoint findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ServiceEndpoint not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<ServiceEndpoint> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<ServiceEndpoint> findByServiceName(String serviceName) {
        return repository.findByServiceName(serviceName);
    }

    @Transactional(readOnly = true)
    public List<ServiceEndpoint> findActive() {
        return repository.findByActiveTrue();
    }

    @Transactional
    public ServiceEndpoint deactivate(Long id) {
        ServiceEndpoint endpoint = findById(id);
        endpoint.setActive(false);
        return repository.save(endpoint);
    }
}
