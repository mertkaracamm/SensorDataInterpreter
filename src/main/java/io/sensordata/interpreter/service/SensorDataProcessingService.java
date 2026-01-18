package io.sensordata.interpreter.service;

import io.sensordata.interpreter.dto.SensorMessageDTO;

public interface SensorDataProcessingService {
    void process(SensorMessageDTO message);
}
