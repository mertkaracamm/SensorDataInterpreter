package io.sensordata.interpreter.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SensorMessageDTO {

	 
    private String id;
    private String type;
    private Double temperature;
    private Double airPressure;
    private Double humidity;
    private Double lightLevel;
    private Double batteryCharge;
    private Double batteryVoltage;

    private List<StatusChangeDTO> statusChanges;
     
    private Double coolingHealth;   //For Light Commercial & Truck
    private Double tyrePressure;    //For Truck

        
    public SensorMessageDTO() {
    }
    // --- getters and setters ---
           
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

    public List<StatusChangeDTO> getStatusChanges() {
        return statusChanges;
    }

    public void setStatusChanges(List<StatusChangeDTO> statusChanges) {
        this.statusChanges = statusChanges;
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
	
}
