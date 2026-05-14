package com.observability.core.controller;

import com.observability.core.dto.LatencyRecordResponse;
import com.observability.core.dto.RecordLatencyRequest;
import com.observability.core.mapper.LatencyRecordMapper;
import com.observability.core.service.LatencyRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/endpoints/{endpointId}/latency")
@RequiredArgsConstructor
public class LatencyRecordController {

    private final LatencyRecordService service;
    private final LatencyRecordMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LatencyRecordResponse record(@PathVariable Long endpointId,
                                        @Valid @RequestBody RecordLatencyRequest request) {
        return mapper.toResponse(service.record(endpointId, request.getLatencyMs(), request.getStatusCode(), request.getTraceId()));
    }

    @GetMapping
    public List<LatencyRecordResponse> findByTimeRange(
            @PathVariable Long endpointId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return service.findByEndpointAndTimeRange(endpointId, from, to)
                .stream().map(mapper::toResponse).toList();
    }
}
