package io.sensordata.interpreter.simulator;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import io.sensordata.interpreter.dto.EventLocationDTO;
import io.sensordata.interpreter.dto.GeometryDTO;
import io.sensordata.interpreter.dto.SensorMessageDTO;
import io.sensordata.interpreter.dto.StatusChangeDTO;
import io.sensordata.interpreter.service.SensorDataProcessingService;

//Inserts one sample operational record when application starts.
@Component
public class DataSeeder implements CommandLineRunner {

    private final SensorDataProcessingService service;

    public DataSeeder(SensorDataProcessingService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) {

        SensorMessageDTO message = new SensorMessageDTO();
        message.setId("01");
        message.setType("HUD21");

       
        
        message.setTemperature(26.09);
        message.setAirPressure(101573.00);
        message.setHumidity(12.09);
        message.setLightLevel(45145.0);
        message.setBatteryCharge(12.09);
        message.setBatteryVoltage(12.12);

        /*message.setCoolingHealth(null);
        message.setTyrePressure(null);*/
        
        // --- Status Change ---
        StatusChangeDTO sc = new StatusChangeDTO();
        sc.setDeviceId("56jddfg-44543-fgdfgs-353444353");
        sc.setVehicleId("56790077");            
        sc.setVehicleType("TPS678");            
        sc.setPropulsionType(Arrays.asList("electric"));
        sc.setEventType("available");           
        sc.setEventTypeReason("user_drop_off");
        sc.setEventTime(15472345678L);

        // --- Location ---
        GeometryDTO geometry = new GeometryDTO();
        geometry.setType("Point");
        geometry.setCoordinates(Arrays.asList(-85.7865, 35.6757657678));

        EventLocationDTO location = new EventLocationDTO();
        location.setGeometry(geometry);

        sc.setEventLocation(location);

        message.setStatusChanges(Collections.singletonList(sc));

        service.process(message);

        System.out.println("DataSeeder inserted 1 operational record.");
    }
}

