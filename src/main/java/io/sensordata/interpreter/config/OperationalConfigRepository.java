package io.sensordata.interpreter.config;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationalConfigRepository
        extends JpaRepository<OperationalConfigEntity, Long> {
}
