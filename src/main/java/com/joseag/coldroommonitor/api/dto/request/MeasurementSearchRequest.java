package com.joseag.coldroommonitor.api.dto.request;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record MeasurementSearchRequest(

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime from,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime to
) {}
