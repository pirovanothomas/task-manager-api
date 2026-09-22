package com.example.taskmanagerapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Task returned by the API")
public class TaskResponse {

    @Schema(
            description = "Unique identifier of the task",
            example = "1"
    )
    private Long id;
    @Schema(
            description = "Title of the task",
            example = "Complete OpenAPI documentation"
    )
    private String title;
    @Schema(
            description = "Indicates whether the task is completed",
            example = "false"
    )
    private boolean completed;

    public TaskResponse(Long id, String title, boolean completed) {
        this.id = id;
        this.title = title;
        this.completed = completed;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }
}