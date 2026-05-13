package com.observability.core.mapper;

import com.observability.core.domain.model.LatencyRecord;
import com.observability.core.dto.LatencyRecordResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LatencyRecordMapper {

    @Mapping(source = "serviceEndpoint.id", target = "serviceEndpointId")
    LatencyRecordResponse toResponse(LatencyRecord entity);
}
