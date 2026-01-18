package io.sensordata.interpreter.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import io.sensordata.interpreter.calculator.MetricCalculator;
import io.sensordata.interpreter.config.OperationalConfig;
import io.sensordata.interpreter.dto.SensorMessageDTO;
import io.sensordata.interpreter.dto.StatusChangeDTO;
import io.sensordata.interpreter.mapper.OperationalMapper;
import io.sensordata.interpreter.model.OperationalData;
import io.sensordata.interpreter.model.StatisticalData;
import io.sensordata.interpreter.repository.OperationalDataRepository;
import io.sensordata.interpreter.repository.StatisticalDataRepository;
import io.sensordata.interpreter.service.ExternalConfigService;
import io.sensordata.interpreter.service.SensorDataProcessingService;

@Service
public class SensorDataProcessingServiceImpl implements SensorDataProcessingService {

    private final OperationalDataRepository operationalRepository;
    private final StatisticalDataRepository statisticalRepository;
    private final ExternalConfigService externalConfigService;
    private final MetricCalculator metricCalculator;
    private final OperationalMapper operationalMapper;


    public SensorDataProcessingServiceImpl(OperationalDataRepository operationalRepository,
    		StatisticalDataRepository statisticalRepository,
            ExternalConfigService externalConfigService,
            MetricCalculator metricCalculator,
            OperationalMapper operationalMapper) {
						this.operationalRepository = operationalRepository;
						this.statisticalRepository = statisticalRepository;
						this.externalConfigService = externalConfigService;
						this.metricCalculator = metricCalculator;
						this.operationalMapper = operationalMapper;
    		}

    @Override
    public void process(SensorMessageDTO message) {

        
        if (message == null) {
            return; // Ignore null messages coming from queue.
        }

        // If statusChanges exists, this is an operational message.
        boolean isOperational =
                message.getStatusChanges() != null && !message.getStatusChanges().isEmpty();

        if (isOperational) {
            handleOperational(message);  // Alarm check + save.
        } else {
            handleStatistical(message);  // Persist raw statistical measurements.
        }
    }

    private void handleStatistical(SensorMessageDTO message) {

    	 if (message.getType() == null || message.getType().isBlank()) {
    	        throw new IllegalArgumentException("Statistical message must contain 'type'");
    	    }

    	 if (message.getTemperature() == null &&
    	        message.getAirPressure() == null &&
    	        message.getHumidity() == null &&
    	        message.getLightLevel() == null &&
    	        message.getBatteryCharge() == null &&
    	        message.getBatteryVoltage() == null) {

    	        throw new IllegalArgumentException("Statistical message must contain at least one measurement field");
    	 }
    	
    	
        StatisticalData data = new StatisticalData();

        data.setId(message.getId());
        data.setType(message.getType());

        data.setTemperature(message.getTemperature());
        data.setAirPressure(message.getAirPressure());
        data.setHumidity(message.getHumidity());
        data.setLightLevel(message.getLightLevel());
        data.setBatteryCharge(message.getBatteryCharge());
        data.setBatteryVoltage(message.getBatteryVoltage());

        data.setCoolingHealth(message.getCoolingHealth());
        data.setTyrePressure(message.getTyrePressure());
        
        data.setRecordTime(LocalDateTime.now());

        statisticalRepository.save(data);
    }



    private void handleOperational(SensorMessageDTO message) {
        // Handle operational message flow: read config, calculate alarms, persist.

        // 1- Take the first status change as main operational event.
        StatusChangeDTO statusChange = message.getStatusChanges().get(0);
        
        if (statusChange.getDeviceId() == null || statusChange.getDeviceId().isBlank()) {
            throw new IllegalArgumentException("Operational message must contain a valid deviceId");
        }

        if (statusChange.getVehicleType() == null) {
            throw new IllegalArgumentException("Operational message must contain vehicleType");
        }

        if (statusChange.getEventLocation() == null ||
            statusChange.getEventLocation().getGeometry() == null ||
            statusChange.getEventLocation().getGeometry().getCoordinates() == null ||
            statusChange.getEventLocation().getGeometry().getCoordinates().size() < 2) {

            throw new IllegalArgumentException("Operational message must contain valid location coordinates");
        }
        
        
        // 2- Read config from external DB (limit, X minutes, Y percent).
        OperationalConfig config = externalConfigService.getConfig();

        // 3- Calculate alarms using rules from PDF.
        boolean batteryAlarm = metricCalculator.isBatteryChargeLow(
                message.getBatteryCharge(), config.getBatteryChargeLimit());

        boolean voltageAlarm = metricCalculator.isVoltageChangeHigh(
                statusChange.getDeviceId(),
                message.getBatteryVoltage(),
                config.getVoltageWindowMinutes(),
                config.getVoltageChangePercent());

        // 4- Map operational data + alarms to entity and persist.
        OperationalData data =
                operationalMapper.toEntity(message, statusChange, batteryAlarm, voltageAlarm);
        
       
        operationalRepository.save(data);
    }




    
}