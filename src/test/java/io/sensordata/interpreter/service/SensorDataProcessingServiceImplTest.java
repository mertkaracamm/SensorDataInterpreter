package io.sensordata.interpreter.service;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.sensordata.interpreter.calculator.MetricCalculator;
import io.sensordata.interpreter.config.OperationalConfig;
import io.sensordata.interpreter.dto.EventLocationDTO;
import io.sensordata.interpreter.dto.GeometryDTO;
import io.sensordata.interpreter.dto.SensorMessageDTO;
import io.sensordata.interpreter.dto.StatusChangeDTO;
import io.sensordata.interpreter.mapper.OperationalMapper;
import io.sensordata.interpreter.model.OperationalData;
import io.sensordata.interpreter.model.StatisticalData;
import io.sensordata.interpreter.repository.OperationalDataRepository;
import io.sensordata.interpreter.repository.StatisticalDataRepository;
import io.sensordata.interpreter.service.impl.SensorDataProcessingServiceImpl;

//Unit tests for message routing in process().
public class SensorDataProcessingServiceImplTest {

    private OperationalDataRepository operationalRepository;
    private StatisticalDataRepository statisticalRepository;
    private ExternalConfigService externalConfigService;
    private MetricCalculator metricCalculator;
    private OperationalMapper operationalMapper;

    private SensorDataProcessingServiceImpl service;

    @BeforeEach
    void setup() {
    	operationalRepository = mock(OperationalDataRepository.class);
        statisticalRepository = mock(StatisticalDataRepository.class);
        externalConfigService = mock(ExternalConfigService.class);
        metricCalculator = mock(MetricCalculator.class);
        operationalMapper = mock(OperationalMapper.class);

        service = new SensorDataProcessingServiceImpl(
        		operationalRepository,statisticalRepository, externalConfigService, metricCalculator, operationalMapper
        );
    }

    @Test
    void process_shouldRouteToStatistical_whenStatusChangesEmpty() {
        // given
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

        service.process(msg);

        // Statistical flow should only save **statisticalRepository** 
        verify(statisticalRepository, times(1)).save(any(StatisticalData.class));

        // Operational dependencies must NOT be used.
        verifyNoInteractions(externalConfigService);
        verifyNoInteractions(metricCalculator);
        verifyNoInteractions(operationalMapper);
        verifyNoInteractions(operationalRepository);
    }


    @Test
    void process_shouldRouteToOperational_whenStatusChangesPresent() {
        // given
        SensorMessageDTO msg = new SensorMessageDTO();
        msg.setId("01");
        msg.setType("HUD21");

        StatusChangeDTO sc = new StatusChangeDTO();
        sc.setDeviceId("56jddfg-44543-fgdfgs-353444353");
        
        sc.setDeviceId("56jddfg-44543-fgdfgs-353444353");
        sc.setVehicleType("TPS678");
        
        EventLocationDTO loc = new EventLocationDTO();
        GeometryDTO geo = new GeometryDTO();
        geo.setCoordinates(Arrays.asList(10.0, 20.0));   // X,Y
        loc.setGeometry(geo);
        sc.setEventLocation(loc);
        
        msg.setStatusChanges(Collections.singletonList(sc));

        // Operational numbers (null pointer önlem)
        msg.setBatteryCharge(15.0);
        msg.setBatteryVoltage(12.5);

        // mock config
        OperationalConfig config = mock(OperationalConfig.class);
        when(config.getBatteryChargeLimit()).thenReturn(20.0);
        when(config.getVoltageWindowMinutes()).thenReturn(5);
        when(config.getVoltageChangePercent()).thenReturn(10.0);

        when(externalConfigService.getConfig()).thenReturn(config);


        // mock metric behaviour
        when(metricCalculator.isBatteryChargeLow(any(), any())).thenReturn(false);
        when(metricCalculator.isVoltageChangeHigh(any(), any(), any(), any())).thenReturn(false);

        // mock mapper
        OperationalData mapped = new OperationalData();
        when(operationalMapper.toEntity(any(), any(), anyBoolean(), anyBoolean()))
                .thenReturn(mapped);

        // when
        service.process(msg);

        // operational flow must run these:
        verify(externalConfigService, times(1)).getConfig();
        verify(metricCalculator, times(1)).isBatteryChargeLow(any(), any());
        verify(metricCalculator, times(1)).isVoltageChangeHigh(any(), any(), any(), any());
        verify(operationalMapper, times(1))
                .toEntity(any(), any(), anyBoolean(), anyBoolean());
        verify(operationalRepository, times(1)).save(mapped);

        // statistical repo should not be touched
        verifyNoInteractions(statisticalRepository);
    }

}
