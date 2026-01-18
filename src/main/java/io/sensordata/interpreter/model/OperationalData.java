package io.sensordata.interpreter.model;


import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "operational_data")
public class OperationalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;

    private String id;
    private String deviceId;
    private String type;

    private String vehicleId;
    private String vehicleType;    
    
    private Double temperature;
    private Double airPressure;
    private Double humidity;
    private Double lightLevel;
    private Double batteryCharge;
    private Double batteryVoltage;
    private Double coolingHealth;   //For Light Commercial & Truck
    private Double tyrePressure;    //For Truck
    private LocalDateTime recordTime;
    
    
              
    // Battery alarm flag for OPERATIONAL records.
    private Boolean batteryAlarm;

    // Voltage alarm flag for OPERATIONAL records.
    private Boolean voltageAlarm;

    // statusChange will be saved as JSON
    @Lob
    private String statusChangeJson;

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

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
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
	
	public LocalDateTime getRecordTime() {
	    return recordTime;
	}

	public void setRecordTime(LocalDateTime recordTime) {
	    this.recordTime = recordTime;
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

	public String getStatusChangeJson() {
		return statusChangeJson;
	}

	public void setStatusChangeJson(String statusChangeJson) {
		this.statusChangeJson = statusChangeJson;
	}

	public Boolean getBatteryAlarm() {
		return batteryAlarm;
	}

	public void setBatteryAlarm(Boolean batteryAlarm) {
		this.batteryAlarm = batteryAlarm;
	}

	public Boolean getVoltageAlarm() {
		return voltageAlarm;
	}

	public void setVoltageAlarm(Boolean voltageAlarm) {
		this.voltageAlarm = voltageAlarm;
	}

    
}
