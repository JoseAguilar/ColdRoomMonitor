package com.joseag.coldroommonitor.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SensorDeviceCreateRequest {

    @NotBlank
    private String name;

    @NotNull
    private Long coldRoomId;

    @NotNull
    private Boolean enabled;

    public String getName() {
        return name;
    }

    public Long getColdRoomId() {
        return coldRoomId;
    }

    public Boolean getEnabled() {
        return enabled;
    }
}
