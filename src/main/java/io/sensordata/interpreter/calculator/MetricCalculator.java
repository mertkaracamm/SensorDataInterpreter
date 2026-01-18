package io.sensordata.interpreter.calculator;

import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.LocalDateTime;

import io.sensordata.interpreter.model.OperationalData;
import io.sensordata.interpreter.repository.OperationalDataRepository;



@Component
public class MetricCalculator {

	private final OperationalDataRepository repository;

	public MetricCalculator(OperationalDataRepository repository) {
	    this.repository = repository; // in order to read previous measurements.
	}

	
    // Rule 1- BatteryCharge.
    public boolean isBatteryChargeLow(Double batteryCharge, Double limit) {
        if (batteryCharge == null || limit == null) {  // false if any value is missing.
            return false;
        }
        return batteryCharge < limit;
    }

    public boolean isVoltageChangeHigh(String deviceId,
            Double currentVoltage,
            Integer windowMinutes,
            Double thresholdPercent) {
					// Rule 2- Compare current voltage with previous one within time window.					
					if (deviceId == null || currentVoltage == null || windowMinutes == null || thresholdPercent == null) {					
						return false;
					}
					
					// Get last saved data for this device.
					OperationalData lastData = repository.findTopByDeviceIdOrderByRecordIdDesc(deviceId);
					if (lastData == null || lastData.getBatteryVoltage() == null || lastData.getRecordTime() == null) {
						return false; // Cannot compute change.
					}
					
					// Check time difference between now and last record.
					LocalDateTime lastTime = lastData.getRecordTime();
					LocalDateTime now = LocalDateTime.now();
					
					long minutesDiff = Duration.between(lastTime, now).toMinutes();
					if (minutesDiff > windowMinutes) {
						return false; // Outside time window -> no alarm.
					}
					
					double previousVoltage = lastData.getBatteryVoltage();
					
					// Division by zero control.
					if (previousVoltage == 0) {
						return false;
					}
					
					// Percent change = |current - previous| / previous * 100
					double percentChange =Math.abs(currentVoltage - previousVoltage) / previousVoltage * 100.0;
										
		return percentChange > thresholdPercent;
	}



}
