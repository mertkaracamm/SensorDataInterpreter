package io.sensordata.interpreter.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.sensordata.interpreter.dto.SensorMessageDTO;
import io.sensordata.interpreter.dto.StatusChangeDTO;
import io.sensordata.interpreter.model.OperationalData;


@Component
public class OperationalMapperImpl implements OperationalMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public OperationalData toEntity(SensorMessageDTO message,
                                         StatusChangeDTO statusChange,
                                         boolean batteryAlarm,
                                         boolean voltageAlarm) {
        // Create entity from operational message and calculated alarms.

        OperationalData data = new OperationalData();

        // Mapping fields
        data.setId(message.getId());
        data.setDeviceId(statusChange.getDeviceId());
        data.setType(message.getType());
        data.setVehicleId(statusChange.getVehicleId());
        data.setVehicleType(statusChange.getVehicleType());
        data.setTemperature(message.getTemperature());
        data.setAirPressure(message.getAirPressure());
        data.setHumidity(message.getHumidity());
        data.setLightLevel(message.getLightLevel());
        data.setBatteryCharge(message.getBatteryCharge());
        data.setBatteryVoltage(message.getBatteryVoltage());
        
        data.setCoolingHealth(message.getCoolingHealth());
        data.setTyrePressure(message.getTyrePressure());
        
        data.setBatteryAlarm(batteryAlarm);
        data.setVoltageAlarm(voltageAlarm);        
        data.setRecordTime(LocalDateTime.now());       

        // Store whole status change as JSON for traceability.
        data.setStatusChangeJson(toJsonSafe(statusChange));

        return data;
    }

    private String toJsonSafe(StatusChangeDTO statusChange) {
        // Convert statusChange to JSON
        try {
            return objectMapper.writeValueAsString(statusChange);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}
