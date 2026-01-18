package io.sensordata.interpreter.service;

import io.sensordata.interpreter.config.OperationalConfig;

//Reads alarm parameters from external DB
public interface ExternalConfigService {

	OperationalConfig getConfig();
}
