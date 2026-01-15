package com.joseag.coldroommonitor.infrastructure.modbus;

import com.joseag.coldroommonitor.domain.model.ColdRoom;
import com.joseag.coldroommonitor.domain.model.SensorDevice;
import com.joseag.coldroommonitor.domain.repository.ColdRoomRepository;
import com.joseag.coldroommonitor.domain.repository.SensorDeviceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ColdRoomRepository coldRoomRepo;
    private final SensorDeviceRepository sensorRepo;

    public DataInitializer(ColdRoomRepository coldRoomRepo, SensorDeviceRepository sensorRepo) {
        this.coldRoomRepo = coldRoomRepo;
        this.sensorRepo = sensorRepo;
    }

    @Override
    public void run(String... args) {
        if (coldRoomRepo.count() == 0) {
            ColdRoom room1 = new ColdRoom("Camara Congelación #1","Planta A" );
            coldRoomRepo.save(room1);

            SensorDevice s1 = new SensorDevice();
            s1.setName("EKC-1");
            s1.setColdRoom(room1);
            s1.setEnabled(true);
            sensorRepo.save(s1);

            SensorDevice s2 = new SensorDevice();
            s2.setName("EKC-2");
            s2.setColdRoom(room1);
            s2.setEnabled(true);
            sensorRepo.save(s2);


            ColdRoom room2 = new ColdRoom("Camara Congelacion #2", "Planta A");
            coldRoomRepo.save(room2);

            SensorDevice s3 = new SensorDevice();
            s3.setName("EKC-3");
            s3.setColdRoom(room2);
            s3.setEnabled(true);
            sensorRepo.save(s3);

        }
    }
}
