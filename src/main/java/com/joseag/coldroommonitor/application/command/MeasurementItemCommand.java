package com.joseag.coldroommonitor.application.command;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MeasurementItemCommand(
        BigDecimal currentValue,
        LocalDateTime measuredAt
) {
}
