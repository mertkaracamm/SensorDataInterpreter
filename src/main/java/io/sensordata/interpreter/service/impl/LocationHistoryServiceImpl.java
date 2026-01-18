package io.sensordata.interpreter.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.sensordata.interpreter.dto.EventLocationDTO;
import io.sensordata.interpreter.dto.StatusChangeDTO;
import io.sensordata.interpreter.exception.NotFoundException;
import io.sensordata.interpreter.model.OperationalData;
import io.sensordata.interpreter.repository.OperationalDataRepository;
import io.sensordata.interpreter.service.LocationHistoryService;

/**
 * Default implementation for location history query.
 * Reads locations from stored statusChangeJson.
 */
@Service
public class LocationHistoryServiceImpl implements LocationHistoryService {

    private final OperationalDataRepository repository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LocationHistoryServiceImpl(OperationalDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<EventLocationDTO> getLocationHistory(String deviceId,
                                                     LocalDateTime startTime,
                                                     LocalDateTime endTime) {
        // Read records from DB and extract eventLocation from statusChangeJson.

        List<OperationalData> records =
        		repository.findByDeviceIdAndRecordTimeBetweenOrderByRecordTimeAsc(deviceId, startTime, endTime);

        if (records.isEmpty()) {
            throw new NotFoundException(
                "No location history found for deviceId=" + deviceId +
                " between " + startTime + " and " + endTime
            );
        }

        List<EventLocationDTO> locations = new ArrayList<>();

        for (OperationalData record : records) {

            String json = record.getStatusChangeJson();
            if (json == null || json.isBlank() || "{}".equals(json)) {
                continue; // statistical or empty operational record
            }

            try {
                StatusChangeDTO statusChange = objectMapper.readValue(json, StatusChangeDTO.class);
                if (statusChange.getEventLocation() != null) {
                    locations.add(statusChange.getEventLocation());
                }
            } catch (Exception e) {
                // If json is broken, skip that record safely.
                continue;
            }
        }

        return locations;
    }
}
