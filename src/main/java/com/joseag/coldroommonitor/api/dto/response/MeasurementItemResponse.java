package com.joseag.coldroommonitor.api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MeasurementItemResponse(
        BigDecimal value,
        LocalDateTime measuredAt
) {
}
