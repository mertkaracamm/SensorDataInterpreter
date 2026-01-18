package io.sensordata.interpreter.calculator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.sensordata.interpreter.model.OperationalData;
import io.sensordata.interpreter.repository.OperationalDataRepository;

//Unit tests for MetricCalculator alarm rules.
public class MetricCalculatorTest {

    private OperationalDataRepository repository;
    private MetricCalculator calculator;

    @BeforeEach
    void setup() {
        repository = mock(OperationalDataRepository.class);
        calculator = new MetricCalculator(repository);
    }

    @Test
    void batteryChargeLow_shouldReturnTrue_whenBelowLimit() {
        assertTrue(calculator.isBatteryChargeLow(10.0, 20.0));
    }

    @Test
    void batteryChargeLow_shouldReturnFalse_whenEqualOrAboveLimit() {
        assertFalse(calculator.isBatteryChargeLow(20.0, 20.0));
        assertFalse(calculator.isBatteryChargeLow(25.0, 20.0));
    }

    @Test
    void voltageChangeHigh_shouldReturnFalse_whenNoPreviousData() {
        when(repository.findTopByDeviceIdOrderByRecordIdDesc("56jddfg-44543-fgdfgs-353444353")).thenReturn(null);

        boolean result = calculator.isVoltageChangeHigh("56jddfg-44543-fgdfgs-353444353", 12.0, 5, 10.0);
        assertFalse(result);
    }

    @Test
    void voltageChangeHigh_shouldReturnFalse_whenOutsideWindow() {
        OperationalData last = new OperationalData();
        last.setBatteryVoltage(10.0);
        last.setRecordTime(LocalDateTime.now().minusMinutes(10)); // older than X=5

        when(repository.findTopByDeviceIdOrderByRecordIdDesc("56jddfg-44543-fgdfgs-353444353")).thenReturn(last);

        boolean result = calculator.isVoltageChangeHigh("56jddfg-44543-fgdfgs-353444353", 12.0, 5, 10.0);
        assertFalse(result);
    }

    @Test
    void voltageChangeHigh_shouldReturnTrue_whenPercentHighInsideWindow() {
        OperationalData last = new OperationalData();
        last.setBatteryVoltage(10.0);
        last.setRecordTime(LocalDateTime.now().minusMinutes(3)); // inside X=5

        when(repository.findTopByDeviceIdOrderByRecordIdDesc("56jddfg-44543-fgdfgs-353444353")).thenReturn(last);

        // change: (12-10)/10 = 20% -> Y=10%
        boolean result = calculator.isVoltageChangeHigh("56jddfg-44543-fgdfgs-353444353", 12.0, 5, 10.0);
        assertTrue(result);
    }

    @Test
    void voltageChangeHigh_shouldReturnFalse_whenPercentLowInsideWindow() {
        OperationalData last = new OperationalData();
        last.setBatteryVoltage(10.0);
        last.setRecordTime(LocalDateTime.now().minusMinutes(3));

        when(repository.findTopByDeviceIdOrderByRecordIdDesc("56jddfg-44543-fgdfgs-353444353")).thenReturn(last);

        // change: (10.5-10)/10 = 5% < Y=10%
        boolean result = calculator.isVoltageChangeHigh("56jddfg-44543-fgdfgs-353444353", 10.5, 5, 10.0);
        assertFalse(result);
    }
    
    @Test
    void voltageChangeHigh_shouldReturnFalse_whenLastVoltageIsNull() {
        OperationalData last = new OperationalData();
        last.setBatteryVoltage(null); // no valid last measurement
        last.setRecordTime(LocalDateTime.now());

        when(repository.findTopByDeviceIdOrderByRecordIdDesc("56jddfg-44543-fgdfgs-353444353")).thenReturn(last);

        boolean result = calculator.isVoltageChangeHigh("56jddfg-44543-fgdfgs-353444353", 12.0, 5, 10.0);
        assertFalse(result);
    }

}
