package com.observability.core.mapper;

import com.observability.core.domain.model.MetricSummary;
import com.observability.core.dto.MetricSummaryResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MetricSummaryMapper {

    MetricSummaryResponse toResponse(MetricSummary domain);
}
