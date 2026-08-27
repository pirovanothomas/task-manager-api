package com.example.taskmanagerapi.service;

import com.example.taskmanagerapi.entity.Task;
import com.example.taskmanagerapi.repository.TaskRepository;
import org.springframework.stereotype.Service;
import com.example.taskmanagerapi.dto.TaskRequest;
import com.example.taskmanagerapi.dto.TaskResponse;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskResponse> getAllTasks() {

        return taskRepository.findAll()
                .stream()
                .map(task -> new TaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.isCompleted()
                ))
                .toList();
    }

    public TaskResponse createTask(TaskRequest request) {

        Task task = new Task();

        task.setTitle(request.getTitle());
        task.setCompleted(request.isCompleted());

        Task savedTask = taskRepository.save(task);

        return new TaskResponse(
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.isCompleted()
        );
    }

    public Optional<TaskResponse> getTaskById(Long id) {

        return taskRepository.findById(id)
                .map(task -> new TaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.isCompleted()
                ));
    }

    public Optional<TaskResponse> updateTask(Long id, TaskRequest request) {

        Optional<Task> existingTask = taskRepository.findById(id);

        if (existingTask.isPresent()) {

            Task taskToUpdate = existingTask.get();

            taskToUpdate.setTitle(request.getTitle());
            taskToUpdate.setCompleted(request.isCompleted());

            Task savedTask = taskRepository.save(taskToUpdate);

            return Optional.of(
                    new TaskResponse(
                            savedTask.getId(),
                            savedTask.getTitle(),
                            savedTask.isCompleted()
                    )
            );
        }

        return Optional.empty();
    }

    public boolean deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }
}