package io.sensordata.interpreter.controller;

import io.sensordata.interpreter.exception.NotFoundException;
import io.sensordata.interpreter.model.StatisticalData;
import io.sensordata.interpreter.repository.StatisticalDataRepository;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StatisticalDataController {

    private final StatisticalDataRepository repository;

    public StatisticalDataController(StatisticalDataRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/statistical-data")
    @Operation(
            summary = "Get Statistical Sensor Data",
            description = "Retrieves statistical data for a given type and/or time range."
    )
    public List<StatisticalData> getStatisticalData(

            @Parameter(description = "Sensor Data Type", example = "HUD21")
            @RequestParam(name = "type", required = false)
            String type,

            @Parameter(description = "Start Time", example = "2025-01-01T00:00:00")
            @RequestParam(name = "startTime", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startTime,

            @Parameter(description = "End Time", example = "2050-12-31T23:59:59")
            @RequestParam(name = "endTime", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endTime
    ) {

        boolean hasType = type != null && !type.isBlank();
        boolean hasTimeRange = startTime != null && endTime != null;

        
        /* Validations*/
        
        if (hasTimeRange && startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("startTime must be before endTime");
        }

        
        if (hasType && !hasTimeRange) {
            return findOrThrow(
                    repository.findByTypeOrderByRecordTimeAsc(type),
                    "No statistical data for type=" + type
            );
        }

        
        if (!hasType && hasTimeRange) {
            return findOrThrow(
                    repository.findByRecordTimeBetweenOrderByRecordTimeAsc(startTime, endTime),
                    "No statistical data in the given time range."
            );
        }

        
        if (hasType && hasTimeRange) {
            return findOrThrow(
                    repository.findByTypeAndRecordTimeBetweenOrderByRecordTimeAsc(type, startTime, endTime),
                    "No statistical data for type=" + type
            );
        }

        
        throw new IllegalArgumentException(
                "Provide either: type OR (startTime + endTime) OR all three together."
        );
        
        /* Validations*/
    }

    private List<StatisticalData> findOrThrow(List<StatisticalData> list, String message) { //Helper for repeat list empty control
        if (list.isEmpty()) {
            throw new NotFoundException(message);
        }
        return list;
    }
}
