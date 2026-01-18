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
import io.sensordata.interpreter.repository.OperationalDataRepository;
import io.sensordata.interpreter.repository.StatisticalDataRepository;
import io.sensordata.interpreter.service.impl.SensorDataProcessingServiceImpl;


public class OperationalHandlerTest {

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
    void operationalMessage_shouldReadConfig_calculateAlarms_mapAndSave() {

        SensorMessageDTO msg = new SensorMessageDTO();
        msg.setId("01");
        msg.setType("HUD21");
        msg.setBatteryCharge(15.0);
        msg.setBatteryVoltage(12.5);

        StatusChangeDTO sc = new StatusChangeDTO();
        sc.setDeviceId("56jddfg-44543-fgdfgs-353444353");
        sc.setVehicleType("TPS678");
        
        EventLocationDTO loc = new EventLocationDTO();
        GeometryDTO geo = new GeometryDTO();
        geo.setCoordinates(Arrays.asList(10.0, 20.0)); 
        loc.setGeometry(geo);
        sc.setEventLocation(loc);

        
        msg.setStatusChanges(Collections.singletonList(sc));

        // -mock config
        OperationalConfig config = mock(OperationalConfig.class);
        when(config.getBatteryChargeLimit()).thenReturn(20.0);
        when(config.getVoltageWindowMinutes()).thenReturn(5);
        when(config.getVoltageChangePercent()).thenReturn(10.0);

        when(externalConfigService.getConfig()).thenReturn(config);

        // mock alarm
        when(metricCalculator.isBatteryChargeLow(15.0, 20.0)).thenReturn(true);
        when(metricCalculator.isVoltageChangeHigh("56jddfg-44543-fgdfgs-353444353", 12.5, 5, 10.0)).thenReturn(false);

        // mock mapper
        OperationalData mapped = new OperationalData();
        when(operationalMapper.toEntity(msg, sc, true, false)).thenReturn(mapped);

        // execute
        service.process(msg);

        // verify
        verify(externalConfigService).getConfig();
        verify(metricCalculator).isBatteryChargeLow(15.0, 20.0);
        verify(metricCalculator).isVoltageChangeHigh("56jddfg-44543-fgdfgs-353444353", 12.5, 5, 10.0);
        verify(operationalMapper).toEntity(msg, sc, true, false);
        verify(operationalRepository).save(mapped);
    }

    @Test
    void operationalMessage_shouldSaveEvenIfNoAlarms() {
        SensorMessageDTO msg = new SensorMessageDTO();
        msg.setId("01");
        msg.setType("HUD21");
        msg.setBatteryCharge(90.0);
        msg.setBatteryVoltage(12.0);

        StatusChangeDTO sc = new StatusChangeDTO();
        sc.setDeviceId("56jddfg-44543-fgdfgs-353444353");
        sc.setVehicleType("TPS678");
        
        EventLocationDTO loc = new EventLocationDTO();
        GeometryDTO geo = new GeometryDTO();
        geo.setCoordinates(Arrays.asList(10.0, 20.0)); 
        loc.setGeometry(geo);
        sc.setEventLocation(loc);
        
        msg.setStatusChanges(Collections.singletonList(sc));

        OperationalConfig config = mock(OperationalConfig.class);
        when(config.getBatteryChargeLimit()).thenReturn(20.0);
        when(config.getVoltageWindowMinutes()).thenReturn(5);
        when(config.getVoltageChangePercent()).thenReturn(10.0);

        when(externalConfigService.getConfig()).thenReturn(config);

        when(metricCalculator.isBatteryChargeLow(90.0, 20.0)).thenReturn(false);
        when(metricCalculator.isVoltageChangeHigh("56jddfg-44543-fgdfgs-353444353", 12.0, 5, 10.0)).thenReturn(false);

        OperationalData mapped = new OperationalData();
        when(operationalMapper.toEntity(msg, sc, false, false)).thenReturn(mapped);

        service.process(msg);

        verify(operationalMapper, times(1))
                .toEntity(msg, sc, false, false);

        verify(operationalRepository, times(1)).save(mapped);
    }
}
