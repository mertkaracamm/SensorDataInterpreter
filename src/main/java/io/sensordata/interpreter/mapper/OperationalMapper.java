package io.sensordata.interpreter.mapper;

import io.sensordata.interpreter.dto.SensorMessageDTO;
import io.sensordata.interpreter.dto.StatusChangeDTO;
import io.sensordata.interpreter.model.OperationalData;

//operational message + alarms
public interface OperationalMapper { 

    OperationalData toEntity(SensorMessageDTO message,
                                  StatusChangeDTO statusChange,
                                  boolean batteryAlarm,
                                  boolean voltageAlarm);
}
