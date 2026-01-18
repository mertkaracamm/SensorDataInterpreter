package io.sensordata.interpreter.controller;

import io.sensordata.interpreter.dto.SensorMessageDTO;
import io.sensordata.interpreter.service.SensorDataProcessingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/messages")
public class MessageIngestController {

    private final SensorDataProcessingService processingService;

    public MessageIngestController(SensorDataProcessingService processingService) {
        this.processingService = processingService;
    }

    @Operation(
            summary = "Ingest a sensor message",
            description = "Accepts a sensor message and routes it automatically",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = SensorMessageDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Message processed and persisted"),
                    @ApiResponse(responseCode = "400", description = "Invalid message format"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping
    public ResponseEntity<String> ingest(@RequestBody SensorMessageDTO message) {

        if (message == null) {
            throw new IllegalArgumentException("Message body cannot be null");
        }

        if (message.getType() == null || message.getType().isBlank()) {
            throw new IllegalArgumentException("Message 'type' field is required");
        }

        processingService.process(message);

        return ResponseEntity.ok("Message processed");
    }
}
