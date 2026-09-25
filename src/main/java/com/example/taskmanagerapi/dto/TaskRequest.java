package com.example.taskmanagerapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data required to create or update a task")
public class TaskRequest {

    @Schema(
            description = "Title of the task",
            example = "Complete OpenAPI documentation",
            minLength = 3,
            maxLength = 100
    )
    @NotBlank(message = "Le titre est obligatoire")
    @Size(
            min = 3,
            max = 100,
            message = "Le titre doit contenir entre 3 et 100 caractères"
    )
    private String title;

    @Schema(
            description = "Completion status of the task",
            example = "false"
    )
    private boolean completed;

    public TaskRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}