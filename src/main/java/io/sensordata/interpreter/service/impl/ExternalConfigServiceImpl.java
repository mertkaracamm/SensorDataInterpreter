package io.sensordata.interpreter.service.impl;

import org.springframework.stereotype.Service;

import io.sensordata.interpreter.config.OperationalConfig;
import io.sensordata.interpreter.config.OperationalConfigEntity;
import io.sensordata.interpreter.config.OperationalConfigRepository;
import io.sensordata.interpreter.service.ExternalConfigService;


@Service
public class ExternalConfigServiceImpl implements ExternalConfigService {

	private final OperationalConfigRepository configRepository;

	public ExternalConfigServiceImpl(OperationalConfigRepository configRepository) {
	    this.configRepository = configRepository;
	}

	
	@Override
	public OperationalConfig getConfig() {
	    OperationalConfigEntity entity = configRepository.findById(1L)
	            .orElseThrow(() -> new IllegalStateException("Operational config not found"));

	    return new OperationalConfig(
	            entity.getBatteryChargeLimit(),
	            entity.getVoltageWindowMinutes(),
	            entity.getVoltageChangePercent()
	    );
	}

}
