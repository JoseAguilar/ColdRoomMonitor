package com.joseag.coldroommonitor.application.command;

import java.time.LocalDateTime;

public record SearchMeasurementCommand(
        LocalDateTime from,
        LocalDateTime to
) {}
