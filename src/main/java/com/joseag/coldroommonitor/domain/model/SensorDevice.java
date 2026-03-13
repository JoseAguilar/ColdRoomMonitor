package com.joseag.coldroommonitor.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.joseag.coldroommonitor.application.command.UpdateSensorDeviceCommand;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class SensorDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sensor_id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coldrooom_id", nullable = false)
    @JsonIgnore
    private ColdRoom coldRoom;

    private boolean enabled = true;

    @OneToMany(mappedBy = "sensor")
    @JsonIgnore
    private List<Measurement> measurements = new ArrayList<>();

    protected SensorDevice(){}

    public SensorDevice(String name, ColdRoom coldRoom, boolean enabled){
        this.name = name;
        this.coldRoom = coldRoom;
        this.enabled = enabled;
    }


    @JsonProperty("coldRoomId")
    public Long getIdOfColdRoom(){
        return coldRoom != null ? coldRoom.getId() : null;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    public Long getId() {
        return sensor_id;
    }

    public void setId(Long id) {
        this.sensor_id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ColdRoom getColdRoom() {
        return coldRoom;
    }

    public void setColdRoom(ColdRoom coldRoom) {
        this.coldRoom = coldRoom;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void update(UpdateSensorDeviceCommand command){
        if (command.name() != null){
            this.name = command.name();
        }
        if (command.enabled() != null){
            this.enabled = command.enabled();
        }
    }
}
