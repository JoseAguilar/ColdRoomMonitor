package com.joseag.coldroommonitor.application.command;

import java.util.List;

public record CreateMeasurementCommand(
        Long id,
        List<MeasurementItemCommand> measurements
) {}
