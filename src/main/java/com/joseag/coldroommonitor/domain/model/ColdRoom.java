package com.joseag.coldroommonitor.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.joseag.coldroommonitor.api.dto.request.ColdRoomUpdateRequest;
import com.joseag.coldroommonitor.application.command.UpdateColdRoomCommand;
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

    protected ColdRoom(){}

    public ColdRoom(String name, String location) {
        this.name = name;
        this.location = location;
    }

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

    public void update(UpdateColdRoomCommand command){
        if (command.name() != null){
            this.name = command.name();
        }

        if (command.location() != null){
            this.location = command.location();
        }

        if (command.enabled() != null){
            this.enabled = command.enabled();
        }
    }
}
