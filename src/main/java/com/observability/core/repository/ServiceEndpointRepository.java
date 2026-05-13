package com.observability.core.repository;

import com.observability.core.domain.model.ServiceEndpoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceEndpointRepository extends JpaRepository<ServiceEndpoint, Long> {

    Optional<ServiceEndpoint> findByServiceNameAndEndpointPathAndHttpMethod(
            String serviceName, String endpointPath, String httpMethod);

    List<ServiceEndpoint> findByServiceName(String serviceName);

    List<ServiceEndpoint> findByActiveTrue();

    boolean existsByServiceNameAndEndpointPathAndHttpMethod(
            String serviceName, String endpointPath, String httpMethod);
}
