package com.joseag.coldroommonitor.api.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MeasurementCreateRequest(
        @NotNull
        Long sensorId,

        @NotEmpty
        List<@Valid MeasurementItemRequest> measurements
) {}
