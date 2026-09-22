package com.example.taskmanagerapi.controller;

import com.example.taskmanagerapi.dto.TaskRequest;
import com.example.taskmanagerapi.dto.TaskResponse;
import com.example.taskmanagerapi.exception.ErrorResponse;
import com.example.taskmanagerapi.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Tag(
        name = "Tasks",
        description = "Operations for managing tasks"
)
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(
            summary = "Get all tasks",
            description = "Returns the list of all tasks."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Tasks successfully retrieved"
    )
    @GetMapping
    public List<TaskResponse> getTasks() {
        return taskService.getAllTasks();
    }

    @Operation(
            summary = "Get a task by ID",
            description = "Returns a task corresponding to the provided ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Task successfully retrieved"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(
            @Parameter(
                    description = "ID of the task",
                    example = "1"
            )
            @PathVariable Long id) {

        Optional<TaskResponse> task = taskService.getTaskById(id);

        if (task.isPresent()) {
            return ResponseEntity.ok(task.get());
        }

        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Create a task",
            description = "Creates a new task."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Task successfully created"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid task data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody TaskRequest request) {

        TaskResponse createdTask = taskService.createTask(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdTask.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(createdTask);
    }

    @Operation(
            summary = "Update a task",
            description = "Updates an existing task."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Task successfully updated"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid task data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @Parameter(
                    description = "ID of the task",
                    example = "1"
            )
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request) {

        Optional<TaskResponse> updatedTask =
                taskService.updateTask(id, request);

        if (updatedTask.isPresent()) {
            return ResponseEntity.ok(updatedTask.get());
        }

        return ResponseEntity.notFound().build();
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Task successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @Parameter(
                    description = "ID of the task",
                    example = "1"
            )
            @PathVariable Long id) {

        boolean deleted = taskService.deleteTask(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}