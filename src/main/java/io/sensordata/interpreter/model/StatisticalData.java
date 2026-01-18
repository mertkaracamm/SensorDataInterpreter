package io.sensordata.interpreter.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "statistical_data")
public class StatisticalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;

    private String id;
    private String type;

    private Double temperature;
    private Double airPressure;
    private Double humidity;
    private Double lightLevel;
    private Double batteryCharge;
    private Double batteryVoltage;    
    private Double coolingHealth;   //For Light Commercial & Truck
    private Double tyrePressure;    //For Truck

    private LocalDateTime recordTime;

	
	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public Double getAirPressure() {
		return airPressure;
	}

	public void setAirPressure(Double airPressure) {
		this.airPressure = airPressure;
	}

	public Double getHumidity() {
		return humidity;
	}

	public void setHumidity(Double humidity) {
		this.humidity = humidity;
	}

	public Double getLightLevel() {
		return lightLevel;
	}

	public void setLightLevel(Double lightLevel) {
		this.lightLevel = lightLevel;
	}

	public Double getBatteryCharge() {
		return batteryCharge;
	}

	public void setBatteryCharge(Double batteryCharge) {
		this.batteryCharge = batteryCharge;
	}

	public Double getBatteryVoltage() {
		return batteryVoltage;
	}

	public void setBatteryVoltage(Double batteryVoltage) {
		this.batteryVoltage = batteryVoltage;
	}

	public Double getCoolingHealth() {
		return coolingHealth;
	}

	public void setCoolingHealth(Double coolingHealth) {
		this.coolingHealth = coolingHealth;
	}

	public Double getTyrePressure() {
		return tyrePressure;
	}

	public void setTyrePressure(Double tyrePressure) {
		this.tyrePressure = tyrePressure;
	}

	public LocalDateTime getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(LocalDateTime recordTime) {
		this.recordTime = recordTime;
	}

   
}
