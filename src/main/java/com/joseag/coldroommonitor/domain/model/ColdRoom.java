package com.joseag.coldroommonitor.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.joseag.coldroommonitor.api.dto.request.ColdRoomUpdateRequest;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class ColdRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coldrooom_id;

    private String name;
    private String location;
    private boolean enabled = true;

    @OneToMany(mappedBy = "coldRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SensorDevice> sensorDeviceList = new ArrayList<>();

    public List<SensorDevice> getSensorDeviceList() {
        return sensorDeviceList;
    }

    public void setSensorDeviceList(List<SensorDevice> sensorDeviceList) {
        this.sensorDeviceList = sensorDeviceList;
    }

    public Long getId() {
        return coldrooom_id;
    }

    public void setId(Long id) {
        this.coldrooom_id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void update(ColdRoomUpdateRequest request){
        if (request.getName() != null){
            this.name = request.getName();
        }

        if (request.getLocation() != null){
            this.location = request.getLocation();
        }

        if (request.getEnabled() != null){
            this.enabled = request.getEnabled();
        }
    }
}
