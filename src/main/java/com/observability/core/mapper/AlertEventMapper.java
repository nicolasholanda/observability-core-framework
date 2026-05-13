package com.observability.core.mapper;

import com.observability.core.domain.model.AlertEvent;
import com.observability.core.dto.AlertEventResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlertEventMapper {

    @Mapping(source = "alertRule.id", target = "alertRuleId")
    AlertEventResponse toResponse(AlertEvent entity);
}
