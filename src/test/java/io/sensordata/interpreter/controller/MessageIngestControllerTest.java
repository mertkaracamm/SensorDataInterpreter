package io.sensordata.interpreter.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


import io.sensordata.interpreter.dto.SensorMessageDTO;
import io.sensordata.interpreter.exception.GlobalExceptionHandler;
import io.sensordata.interpreter.service.SensorDataProcessingService;


@WebMvcTest(
	    controllers = MessageIngestController.class,
	    excludeFilters = @ComponentScan.Filter(
	            type = FilterType.ASSIGNABLE_TYPE,
	            classes = io.swagger.v3.oas.annotations.parameters.RequestBody.class
	    )
	)
	@Import(GlobalExceptionHandler.class)
	class MessageIngestControllerTest {

	    @Autowired
	    private MockMvc mockMvc;

	    @MockBean
	    private SensorDataProcessingService processingService;

	    @Test
	    void shouldIngestMessageAndReturn200() throws Exception {

	        String json = "{\"id\":\"01\",\"type\":\"HUD21\",\"temperature\":22.5}";

	        mockMvc.perform(post("/api/messages")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(json)
	        )
	        .andExpect(status().isOk())
	        .andExpect(content().string("Message processed"));

	        verify(processingService, times(1)).process(any(SensorMessageDTO.class));
	    }
	}




