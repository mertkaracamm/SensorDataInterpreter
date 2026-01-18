package io.sensordata.interpreter.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import io.sensordata.interpreter.dto.EventLocationDTO;
import io.sensordata.interpreter.service.LocationHistoryService;

//Unit tests for LocationHistoryController
@WebMvcTest(LocationHistoryController.class)
public class LocationHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationHistoryService locationHistoryService;

    @Test
    void getLocationHistory_shouldCallServiceAndReturn200() throws Exception {
        // given
        when(locationHistoryService.getLocationHistory(anyString(), any(), any()))
                .thenReturn(Collections.singletonList(new EventLocationDTO()));

        // when + then
        mockMvc.perform(get("/api/location-history")
                        .param("deviceId", "HUD21")
                        .param("startTime", "2025-01-01T10:00:00")
                        .param("endTime", "2025-01-01T12:00:00"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));

        // verify service called with correct params
        verify(locationHistoryService, times(1)).getLocationHistory(
                eq("HUD21"),
                eq(LocalDateTime.parse("2025-01-01T10:00:00")),
                eq(LocalDateTime.parse("2025-01-01T12:00:00"))
        );
    }
}
