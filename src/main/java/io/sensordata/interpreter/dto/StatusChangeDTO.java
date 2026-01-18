package io.sensordata.interpreter.dto;

import java.util.List;


public class StatusChangeDTO {

	private String deviceId;  
    private String vehicleId; 
    private String vehicleType;    
    private List<String> propulsionType;
    private String eventType;
    private String eventTypeReason;
    private Long eventTime;
    private EventLocationDTO eventLocation;
    
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
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
	public List<String> getPropulsionType() {
		return propulsionType;
	}
	public void setPropulsionType(List<String> propulsionType) {
		this.propulsionType = propulsionType;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getEventTypeReason() {
		return eventTypeReason;
	}
	public void setEventTypeReason(String eventTypeReason) {
		this.eventTypeReason = eventTypeReason;
	}
	public Long getEventTime() {
		return eventTime;
	}
	public void setEventTime(Long eventTime) {
		this.eventTime = eventTime;
	}
	public EventLocationDTO getEventLocation() {
		return eventLocation;
	}
	public void setEventLocation(EventLocationDTO eventLocation) {
		this.eventLocation = eventLocation;
	}

    
}
