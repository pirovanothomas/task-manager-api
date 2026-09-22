package com.example.taskmanagerapi.exception;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Standard error response returned by the API")
public class ErrorResponse {

    @Schema(
            description = "HTTP status code",
            example = "400"
    )
    private int status;
    @Schema(
            description = "HTTP error name",
            example = "Validation failed"
    )
    private String error;
    @Schema(
            description = "Detailed error message",
            example = "Le titre doit contenir entre 3 et 100 caractères"
    )
    private String message;

    public ErrorResponse(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}