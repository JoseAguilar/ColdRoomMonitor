package com.joseag.coldroommonitor.api.dto.response;

import java.util.List;

public record ColdRoomMeasurementResponse(
        Long coldRoomId,
        List<SensorMeasurementResponse> sensors
) {}
