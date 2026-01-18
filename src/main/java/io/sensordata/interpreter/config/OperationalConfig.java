package io.sensordata.interpreter.config;

// Holds external DB alarm parameters (battery_charge_limit, voltage_window_minutes, voltage_change_percent).
public class OperationalConfig {

    private Double batteryChargeLimit;
    private Integer voltageWindowMinutes; 
    private Double voltageChangePercent;

    public OperationalConfig(Double batteryChargeLimit, Integer voltageWindowMinutes, Double voltageChangePercent) {
        this.batteryChargeLimit = batteryChargeLimit;
        this.voltageWindowMinutes = voltageWindowMinutes;
        this.voltageChangePercent = voltageChangePercent;
    }

    public Double getBatteryChargeLimit() {
		return batteryChargeLimit;
	}

	public Integer getVoltageWindowMinutes() {
		return voltageWindowMinutes;
	}

	public Double getVoltageChangePercent() {
		return voltageChangePercent;
	}
}
