package io.sensordata.interpreter.config;

import jakarta.persistence.*;

@Entity
@Table(name = "operational_config")
public class OperationalConfigEntity {

    @Id
    private Long id;

    private Double batteryChargeLimit;
    private Integer voltageWindowMinutes;
    private Double voltageChangePercent;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Double getBatteryChargeLimit() {
		return batteryChargeLimit;
	}
	public void setBatteryChargeLimit(Double batteryChargeLimit) {
		this.batteryChargeLimit = batteryChargeLimit;
	}
	public Integer getVoltageWindowMinutes() {
		return voltageWindowMinutes;
	}
	public void setVoltageWindowMinutes(Integer voltageWindowMinutes) {
		this.voltageWindowMinutes = voltageWindowMinutes;
	}
	public Double getVoltageChangePercent() {
		return voltageChangePercent;
	}
	public void setVoltageChangePercent(Double voltageChangePercent) {
		this.voltageChangePercent = voltageChangePercent;
	}

    
}
