package com.joseag.coldroommonitor.infrastructure.modbus;

import com.joseag.coldroommonitor.application.service.MeasurementService;
import com.joseag.coldroommonitor.application.service.SensorService;
import com.joseag.coldroommonitor.domain.model.SensorDevice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataAcquisitionJob {

    private final SensorService sensorService;
    private final FakeModbusClient modbusClient;
    private final MeasurementService measurementService;

    public DataAcquisitionJob(
            SensorService sensorService,
            FakeModbusClient modbusClient,
            MeasurementService measurementService){
        this.sensorService = sensorService;
        this.modbusClient = modbusClient;
        this.measurementService = measurementService;
    }

    @Scheduled(fixedRate = 60000)
    public void pollSensors(){
        List<SensorDevice> sensors = sensorService.getAllActiveSensors();
        for (SensorDevice sensor : sensors){
            Double value = modbusClient.readTemperature(sensor);
            measurementService.saveMeasurement(sensor, value);
        }
        System.out.println("DAQ fake: se generaron mediciones para " +
                sensors.size() +" sensores");
    }

}
