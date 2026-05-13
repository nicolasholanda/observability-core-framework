package com.observability.core.mapper;

import com.observability.core.domain.model.AlertRule;
import com.observability.core.dto.AlertRuleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlertRuleMapper {

    @Mapping(source = "serviceEndpoint.id", target = "serviceEndpointId")
    AlertRuleResponse toResponse(AlertRule entity);
}
