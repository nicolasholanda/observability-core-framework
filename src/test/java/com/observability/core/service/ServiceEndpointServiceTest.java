package com.observability.core.service;

import com.observability.core.domain.model.ServiceEndpoint;
import com.observability.core.exception.ConflictException;
import com.observability.core.exception.ResourceNotFoundException;
import com.observability.core.repository.ServiceEndpointRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceEndpointServiceTest {

    @Mock
    private ServiceEndpointRepository repository;

    @InjectMocks
    private ServiceEndpointService service;

    @Test
    void create_shouldSaveEndpoint_whenNotExists() {
        when(repository.existsByServiceNameAndEndpointPathAndHttpMethod("svc", "/api", "GET")).thenReturn(false);
        ServiceEndpoint saved = ServiceEndpoint.builder().id(1L).serviceName("svc").endpointPath("/api").httpMethod("GET").build();
        when(repository.save(any())).thenReturn(saved);

        ServiceEndpoint result = service.create("svc", "/api", "GET");

        assertThat(result.getId()).isEqualTo(1L);
        verify(repository).save(any());
    }

    @Test
    void create_shouldThrowConflictException_whenAlreadyExists() {
        when(repository.existsByServiceNameAndEndpointPathAndHttpMethod("svc", "/api", "GET")).thenReturn(true);

        assertThatThrownBy(() -> service.create("svc", "/api", "GET"))
                .isInstanceOf(ConflictException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void findById_shouldReturnEndpoint_whenFound() {
        ServiceEndpoint endpoint = ServiceEndpoint.builder().id(1L).build();
        when(repository.findById(1L)).thenReturn(Optional.of(endpoint));

        assertThat(service.findById(1L)).isEqualTo(endpoint);
    }

    @Test
    void findById_shouldThrowResourceNotFoundException_whenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void findAll_shouldReturnAllEndpoints() {
        List<ServiceEndpoint> endpoints = List.of(ServiceEndpoint.builder().id(1L).build());
        when(repository.findAll()).thenReturn(endpoints);

        assertThat(service.findAll()).hasSize(1);
    }

    @Test
    void deactivate_shouldSetActiveFalse() {
        ServiceEndpoint endpoint = ServiceEndpoint.builder().id(1L).active(true).build();
        when(repository.findById(1L)).thenReturn(Optional.of(endpoint));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ServiceEndpoint result = service.deactivate(1L);

        assertThat(result.isActive()).isFalse();
    }
}
