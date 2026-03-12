package com.joseag.coldroommonitor.api.dto.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MeasurementItemRequest(

        @NotNull
        BigDecimal currentValue,

        @NotNull
        LocalDateTime measuredAt

) {}
