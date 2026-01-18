package io.sensordata.interpreter.service;

import java.time.LocalDateTime;
import java.util.List;

import io.sensordata.interpreter.dto.EventLocationDTO;

//Provides location history of a device for a time range.
public interface LocationHistoryService {

    List<EventLocationDTO> getLocationHistory(String deviceId,
                                              LocalDateTime startTime,
                                              LocalDateTime endTime);
}
