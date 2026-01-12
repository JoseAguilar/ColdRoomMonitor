package com.joseag.coldroommonitor.api.dto.request;


import jakarta.validation.constraints.NotBlank;

public class ColdRoomCreateRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String location;

    @NotBlank
    private Boolean enabled;

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Boolean getEnabled() {
        return enabled;
    }
}
