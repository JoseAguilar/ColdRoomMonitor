package com.joseag.coldroommonitor.infrastructure.exception;

public record ApiError(
        String code,
        String message
) {
}
