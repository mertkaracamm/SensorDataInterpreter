package io.sensordata.interpreter.repository;

import io.sensordata.interpreter.model.OperationalData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface OperationalDataRepository extends JpaRepository<OperationalData, Long> {

    // Gets the latest processed data for a device.
    OperationalData findTopByDeviceIdOrderByRecordIdDesc(String deviceId);

    // Returns records of a device within given time range.
    List<OperationalData> findByDeviceIdAndRecordTimeBetweenOrderByRecordTimeAsc(
            String deviceId,
            LocalDateTime startTime,
            LocalDateTime endTime
    );

    // Returns records by only time range (deviceId not provided).
    List<OperationalData> findByRecordTimeBetweenOrderByRecordTimeAsc(
            LocalDateTime startTime,
            LocalDateTime endTime
    );

    // Returns all records for a given deviceId (no time range).
    List<OperationalData> findByDeviceIdOrderByRecordTimeAsc(String deviceId);
}
