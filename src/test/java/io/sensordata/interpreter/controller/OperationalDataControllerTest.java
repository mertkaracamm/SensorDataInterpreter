package io.sensordata.interpreter.controller;

import io.sensordata.interpreter.model.OperationalData;
import io.sensordata.interpreter.repository.OperationalDataRepository;
import io.sensordata.interpreter.exception.GlobalExceptionHandler;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OperationalDataController.class)
@Import(GlobalExceptionHandler.class)
class OperationalDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OperationalDataRepository repository;

    @Test
    void shouldReturnOperationalData_whenDeviceIdProvided() throws Exception {

        OperationalData record = new OperationalData();
        record.setDeviceId("HUD21");

        when(repository.findByDeviceIdOrderByRecordTimeAsc("HUD21"))
                .thenReturn(List.of(record));

        mockMvc.perform(get("/api/operational-data")
                        .param("deviceId", "HUD21"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn404_whenNoOperationalDataFound() throws Exception {

        when(repository.findByDeviceIdOrderByRecordTimeAsc("HUD21"))
                .thenReturn(List.of()); // no data

        mockMvc.perform(get("/api/operational-data")
                        .param("deviceId", "HUD21"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400_whenInvalidParameters() throws Exception {

        mockMvc.perform(get("/api/operational-data"))
                .andExpect(status().isBadRequest());
    }
}
