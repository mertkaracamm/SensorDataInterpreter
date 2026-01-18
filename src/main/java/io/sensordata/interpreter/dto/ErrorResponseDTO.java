package io.sensordata.interpreter.dto;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Standard error response")
public class ErrorResponseDTO {

    @Schema(description = "Error code / title", example = "BAD_REQUEST")
    private String error;

    @Schema(description = "Error message", example = "startTime must be before endTime")
    private String message;

    public ErrorResponseDTO() { }

    public ErrorResponseDTO(String error, String message) {
        this.error = error;
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
