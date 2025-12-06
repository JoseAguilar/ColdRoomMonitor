package com.joseag.coldroommonitor.infrastructure.modbus;

import com.joseag.coldroommonitor.domain.model.SensorDevice;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class FakeModbusClient {

    private final Random random = new Random();

    public Double readTemperature(SensorDevice sensor){
        double result = -25 + (10*random.nextDouble());
        int rounded = (int) (result*10);
        return rounded/10.0;
    }
}
