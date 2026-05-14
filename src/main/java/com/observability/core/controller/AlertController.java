package com.observability.core.controller;

import com.observability.core.dto.AlertEventResponse;
import com.observability.core.dto.AlertRuleResponse;
import com.observability.core.dto.CreateAlertRuleRequest;
import com.observability.core.mapper.AlertEventMapper;
import com.observability.core.mapper.AlertRuleMapper;
import com.observability.core.service.AlertEvaluationService;
import com.observability.core.service.AlertRuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/endpoints/{endpointId}/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertRuleService alertRuleService;
    private final AlertEvaluationService alertEvaluationService;
    private final AlertRuleMapper alertRuleMapper;
    private final AlertEventMapper alertEventMapper;

    @PostMapping("/rules")
    @ResponseStatus(HttpStatus.CREATED)
    public AlertRuleResponse createRule(@PathVariable Long endpointId,
                                        @Valid @RequestBody CreateAlertRuleRequest request) {
        return alertRuleMapper.toResponse(alertRuleService.create(
                endpointId, request.getName(), request.getPercentile(),
                request.getThresholdMs(), request.getSeverity(), request.getWindow()));
    }

    @GetMapping("/rules")
    public List<AlertRuleResponse> getRules(@PathVariable Long endpointId) {
        return alertRuleService.findByEndpoint(endpointId).stream()
                .map(alertRuleMapper::toResponse).toList();
    }

    @PatchMapping("/rules/{ruleId}/disable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disableRule(@PathVariable Long endpointId, @PathVariable Long ruleId) {
        alertRuleService.disable(ruleId);
    }

    @PatchMapping("/rules/{ruleId}/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enableRule(@PathVariable Long endpointId, @PathVariable Long ruleId) {
        alertRuleService.enable(ruleId);
    }

    @GetMapping("/events")
    public List<AlertEventResponse> getEvents(
            @PathVariable Long endpointId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return alertEvaluationService.findEventsByEndpoint(endpointId, from, to)
                .stream().map(alertEventMapper::toResponse).toList();
    }
}
