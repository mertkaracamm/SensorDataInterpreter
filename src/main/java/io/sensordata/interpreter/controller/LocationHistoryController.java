package io.sensordata.interpreter.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import io.sensordata.interpreter.dto.EventLocationDTO;
import io.sensordata.interpreter.service.LocationHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/location-history")
public class LocationHistoryController {

    private final LocationHistoryService service;

    public LocationHistoryController(LocationHistoryService service) {
        this.service = service;
    }

    @Operation(
        summary = "Get Location History of a Device",
        description = "Returns event locations of the given device within the provided time range."
    )
    @GetMapping
    public List<EventLocationDTO> getLocationHistory(
            @Parameter(description = "Device ID", example = "56jddfg-44543-fgdfgs-353444353")
            @RequestParam String deviceId,

            @Parameter(description = "Start Time", example = "2025-01-01T00:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startTime,

            @Parameter(description = "End Time", example = "2050-01-01T12:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endTime
    ) {

        validate(deviceId, startTime, endTime);

        return service.getLocationHistory(deviceId, startTime, endTime);
    }

    
    private void validate(String deviceId, LocalDateTime start, LocalDateTime end) {

        if (deviceId == null || deviceId.isBlank()) {
            throw new IllegalArgumentException("deviceId must not be empty.");
        }

        if (start == null || end == null) {
            throw new IllegalArgumentException("startTime and endTime are required.");
        }

        if (start.isAfter(end)) {
            throw new IllegalArgumentException("startTime must be before endTime.");
        }
    }
}
