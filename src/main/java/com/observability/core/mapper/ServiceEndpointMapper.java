package com.observability.core.mapper;

import com.observability.core.domain.model.ServiceEndpoint;
import com.observability.core.dto.ServiceEndpointResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceEndpointMapper {

    ServiceEndpointResponse toResponse(ServiceEndpoint entity);
}
