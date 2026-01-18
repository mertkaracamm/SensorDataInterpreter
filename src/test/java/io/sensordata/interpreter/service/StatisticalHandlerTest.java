package io.sensordata.interpreter.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import io.sensordata.interpreter.calculator.MetricCalculator;
import io.sensordata.interpreter.dto.SensorMessageDTO;
import io.sensordata.interpreter.mapper.OperationalMapper;
import io.sensordata.interpreter.model.StatisticalData;
import io.sensordata.interpreter.repository.OperationalDataRepository;
import io.sensordata.interpreter.repository.StatisticalDataRepository;
import io.sensordata.interpreter.service.impl.SensorDataProcessingServiceImpl;


public class StatisticalHandlerTest {

    private OperationalDataRepository operationalRepository;
    private StatisticalDataRepository statisticalRepository;
    private ExternalConfigService externalConfigService;
    private MetricCalculator metricCalculator;
    private OperationalMapper operationalMapper;

    private SensorDataProcessingServiceImpl service;

    @BeforeEach
    void setup() {
    	operationalRepository = mock(OperationalDataRepository.class);
    	statisticalRepository= mock(StatisticalDataRepository.class);
        externalConfigService = mock(ExternalConfigService.class);
        metricCalculator = mock(MetricCalculator.class);
        operationalMapper = mock(OperationalMapper.class);

        service = new SensorDataProcessingServiceImpl(
        		operationalRepository,statisticalRepository, externalConfigService, metricCalculator, operationalMapper
        );
    }

    @Test
    void statisticalMessage_shouldBeSavedWithRawFields() {
        
        SensorMessageDTO msg = new SensorMessageDTO();
        msg.setId("01");
        msg.setType("HUD21");
        msg.setStatusChanges(Collections.emptyList()); // statistical

        msg.setTemperature(22.5);
        msg.setAirPressure(999.0);
        msg.setHumidity(45.0);
        msg.setLightLevel(300.0);
        msg.setBatteryCharge(80.0);
        msg.setBatteryVoltage(12.1);

        //when
        service.process(msg);

        // capture saved entity
        ArgumentCaptor<StatisticalData> captor =
                ArgumentCaptor.forClass(StatisticalData.class);

        verify(statisticalRepository, atLeastOnce()).save(captor.capture());

        StatisticalData saved = captor.getValue();

        assertEquals("01", saved.getId());
        assertEquals("HUD21", saved.getType());

        assertEquals(22.5, saved.getTemperature());
        assertEquals(999.0, saved.getAirPressure());
        assertEquals(45.0, saved.getHumidity());
        assertEquals(300.0, saved.getLightLevel());
        assertEquals(80.0, saved.getBatteryCharge());
        assertEquals(12.1, saved.getBatteryVoltage());

        
        assertNull(saved.getRecordId());
    }
}
