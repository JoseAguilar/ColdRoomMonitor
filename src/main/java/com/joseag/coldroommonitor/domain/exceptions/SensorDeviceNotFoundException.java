package com.joseag.coldroommonitor.domain.exceptions;

public class SensorDeviceNotFoundException extends RuntimeException{
    public SensorDeviceNotFoundException(Long id){
        super("SensorDevice not found with id "+id);
    }
}
