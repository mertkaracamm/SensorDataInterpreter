package io.sensordata.interpreter.controller;

import io.sensordata.interpreter.exception.NotFoundException;
import io.sensordata.interpreter.model.OperationalData;
import io.sensordata.interpreter.repository.OperationalDataRepository;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class OperationalDataController {

    private final OperationalDataRepository repository;

    public OperationalDataController(OperationalDataRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/operational-data")
    @Operation(
            summary = "Get Operational Sensor Data",
            description = "Retrieves operational data for a given deviceId or within a specified time range."
    )
    public List<OperationalData> getOperationalData(

            @Parameter(description = "Device ID", example = "56jddfg-44543-fgdfgs-353444353")
            @RequestParam(name = "deviceId", required = false)
            String deviceId,

            @Parameter(description = "Start Time", example = "2025-01-01T00:00:00")
            @RequestParam(name = "startTime", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startTime,

            @Parameter(description = "End Time", example = "2050-01-01T00:00:00")
            @RequestParam(name = "endTime", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endTime
    ) {

        boolean hasDeviceId = deviceId != null && !deviceId.isBlank();
        boolean hasTimeRange = startTime != null && endTime != null;

        /* Validations*/
                
        if (hasTimeRange && startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("startTime must be before endTime");
        }
        
        if (hasDeviceId && !hasTimeRange) {
            return findOrThrow(
                    repository.findByDeviceIdOrderByRecordTimeAsc(deviceId),
                    "No operational data for deviceId=" + deviceId
            );
        }
        
        if (!hasDeviceId && hasTimeRange) {
            return findOrThrow(
                    repository.findByRecordTimeBetweenOrderByRecordTimeAsc(startTime, endTime),
                    "No operational data in the given time range"
            );
        }

        if (hasDeviceId && hasTimeRange) {
            return findOrThrow(
                    repository.findByDeviceIdAndRecordTimeBetweenOrderByRecordTimeAsc(deviceId, startTime, endTime),
                    "No operational data for deviceId=" + deviceId
            );
        }
        
        throw new IllegalArgumentException(
                "Provide either: deviceId OR (startTime + endTime) OR all three together"
        );
        
        /* Validations*/
    }
   
    private List<OperationalData> findOrThrow(List<OperationalData> list, String errorMessage) { //Helper for repeat list empty control
        if (list.isEmpty()) {
            throw new NotFoundException(errorMessage);
        }
        return list;
    }
}
