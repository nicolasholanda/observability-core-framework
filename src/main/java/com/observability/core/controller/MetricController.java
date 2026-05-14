package com.observability.core.controller;

import com.observability.core.domain.enums.AggregationWindow;
import com.observability.core.dto.MetricSummaryResponse;
import com.observability.core.mapper.MetricSummaryMapper;
import com.observability.core.service.MetricSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/endpoints/{endpointId}/metrics")
@RequiredArgsConstructor
public class MetricController {

    private final MetricSummaryService service;
    private final MetricSummaryMapper mapper;

    @GetMapping
    public MetricSummaryResponse getSummary(@PathVariable Long endpointId,
                                            @RequestParam(defaultValue = "FIVE_MINUTES") AggregationWindow window) {
        return mapper.toResponse(service.computeSummary(endpointId, window));
    }
}
