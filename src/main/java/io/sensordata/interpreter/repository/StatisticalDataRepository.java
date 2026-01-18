package io.sensordata.interpreter.repository;

import io.sensordata.interpreter.model.StatisticalData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticalDataRepository extends JpaRepository<StatisticalData, Long> {

    // Returns all statistical records for the given type
    List<StatisticalData> findByTypeOrderByRecordTimeAsc(String type);

    // Returns statistical records within a given time range
    List<StatisticalData> findByRecordTimeBetweenOrderByRecordTimeAsc(
            LocalDateTime start,
            LocalDateTime end
    );

    // Returns statistical records filtered by type AND time range
    List<StatisticalData> findByTypeAndRecordTimeBetweenOrderByRecordTimeAsc(
            String type,
            LocalDateTime start,
            LocalDateTime end
    );
}
