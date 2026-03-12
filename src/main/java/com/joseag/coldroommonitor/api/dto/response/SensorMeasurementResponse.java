package com.joseag.coldroommonitor.api.dto.response;

import java.util.List;

public record SensorMeasurementResponse(
        Long sensorId,
        List<MeasurementItemResponse> measurements
) {}
