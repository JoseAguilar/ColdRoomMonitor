package com.joseag.coldroommonitor.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_id", nullable = false)
    @JsonIgnore
    private SensorDevice sensor;

    private LocalDateTime timestamp;

    private double value;


    @JsonProperty("sensorId")
    public Long getIdOfSensor(){
        return sensor != null ? sensor.getId() : null;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SensorDevice getSensor() {
        return sensor;
    }

    public void setSensor(SensorDevice sensor) {
        this.sensor = sensor;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
