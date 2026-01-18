package io.sensordata.interpreter.controller;

import io.sensordata.interpreter.exception.GlobalExceptionHandler;
import io.sensordata.interpreter.model.StatisticalData;
import io.sensordata.interpreter.repository.StatisticalDataRepository;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(StatisticalDataController.class)
@Import(GlobalExceptionHandler.class)
class StatisticalDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatisticalDataRepository repository;

    @Test
    void shouldReturnList_whenTypeProvided() throws Exception {

        when(repository.findByTypeOrderByRecordTimeAsc("TEMP"))
                .thenReturn(List.of(new StatisticalData()));

        mockMvc.perform(get("/api/statistical-data")
                        .param("type", "TEMP"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnList_whenTimeRangeProvided() throws Exception {

        when(repository.findByRecordTimeBetweenOrderByRecordTimeAsc(any(), any()))
                .thenReturn(List.of(new StatisticalData()));

        mockMvc.perform(get("/api/statistical-data")
                        .param("startTime", "2025-01-01T10:00:00")
                        .param("endTime", "2025-01-01T12:00:00"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn404_whenDataNotFound() throws Exception {

        when(repository.findByTypeOrderByRecordTimeAsc("TEMP"))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/statistical-data")
                        .param("type", "TEMP"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400_whenMissingAllParameters() throws Exception {
        mockMvc.perform(get("/api/statistical-data"))
                .andExpect(status().isBadRequest());
    }
}
