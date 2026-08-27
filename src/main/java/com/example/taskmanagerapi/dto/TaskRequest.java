package com.example.taskmanagerapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TaskRequest {

    @NotBlank(message = "Le titre est obligatoire")
    @Size(
            min = 3,
            max = 100,
            message = "Le titre doit contenir entre 3 et 100 caractères"
    )
    private String title;
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