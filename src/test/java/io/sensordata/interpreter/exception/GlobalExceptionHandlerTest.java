package io.sensordata.interpreter.exception;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import io.sensordata.interpreter.service.LocationHistoryService;
import io.sensordata.interpreter.controller.LocationHistoryController;
import org.springframework.context.annotation.Import;



@WebMvcTest(controllers = LocationHistoryController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationHistoryService locationHistoryService;

    @Test
    void shouldReturn400_whenIllegalArgumentExceptionThrown() throws Exception {
        when(locationHistoryService.getLocationHistory(anyString(), any(), any()))
                .thenThrow(new IllegalArgumentException("startTime must be before endTime"));

        mockMvc.perform(get("/api/location-history")
                        .param("deviceId", "HUD21")
                        .param("startTime", "2025-01-01T12:00:00")
                        .param("endTime", "2025-01-01T10:00:00"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("startTime must be before endTime."));
    }


    @Test
    void shouldReturn404_whenNotFoundExceptionThrown() throws Exception {
        when(locationHistoryService.getLocationHistory(anyString(), any(), any()))
                .thenThrow(new NotFoundException("not found"));

        mockMvc.perform(get("/api/location-history")
                        .param("deviceId", "HUD21")
                        .param("startTime", "2025-01-01T12:00:00")
                        .param("endTime", "2025-01-01T13:00:00"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("not found"));
    }


    @Test
    void shouldReturn500_whenUnexpectedExceptionThrown() throws Exception {
        when(locationHistoryService.getLocationHistory(anyString(), any(), any()))
                .thenThrow(new RuntimeException("boom"));

        mockMvc.perform(get("/api/location-history")
                        .param("deviceId", "HUD21")
                        .param("startTime", "2025-01-01T12:00:00")
                        .param("endTime", "2025-01-01T13:00:00"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("INTERNAL_ERROR"))
                .andExpect(jsonPath("$.message").value("Unexpected error occurred"));
    }
}
