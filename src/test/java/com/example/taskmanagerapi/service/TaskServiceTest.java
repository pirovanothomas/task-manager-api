package com.example.taskmanagerapi.service;

import com.example.taskmanagerapi.repository.TaskRepository;
import com.example.taskmanagerapi.entity.Task;
import com.example.taskmanagerapi.dto.TaskResponse;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import com.example.taskmanagerapi.dto.TaskRequest;
import com.example.taskmanagerapi.dto.TaskResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import org.mockito.ArgumentCaptor;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService(taskRepository);
    }

    @Test
    void getAllTasks_shouldReturnTasks() {

        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Apprendre Spring Boot");
        task1.setCompleted(false);

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Apprendre Mockito");
        task2.setCompleted(true);

        when(taskRepository.findAll())
                .thenReturn(List.of(task1, task2));

        List<TaskResponse> result = taskService.getAllTasks();

        assertEquals(2, result.size());

        assertEquals(1L, result.get(0).getId());
        assertEquals("Apprendre Spring Boot", result.get(0).getTitle());
        assertEquals(false, result.get(0).isCompleted());

        assertEquals(2L, result.get(1).getId());
        assertEquals("Apprendre Mockito", result.get(1).getTitle());
        assertEquals(true, result.get(1).isCompleted());

        verify(taskRepository).findAll();
    }

    @Test
    void getTaskById_shouldReturnTask_whenTaskExists() {

        Task task = new Task();
        task.setId(1L);
        task.setTitle("Apprendre Spring Boot");
        task.setCompleted(false);

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        Optional<TaskResponse> result = taskService.getTaskById(1L);

        assertTrue(result.isPresent());

        assertEquals(1L, result.get().getId());
        assertEquals("Apprendre Spring Boot", result.get().getTitle());
        assertEquals(false, result.get().isCompleted());

        verify(taskRepository).findById(1L);
    }

    @Test
    void getTaskById_shouldReturnEmpty_whenTaskDoesNotExist() {

        when(taskRepository.findById(999L))
                .thenReturn(Optional.empty());

        Optional<TaskResponse> result = taskService.getTaskById(999L);

        assertTrue(result.isEmpty());

        verify(taskRepository).findById(999L);
    }

    @Test
    void createTask_shouldCreateAndReturnTask() {

        TaskRequest request = new TaskRequest();
        request.setTitle("Apprendre JUnit");
        request.setCompleted(false);

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle("Apprendre JUnit");
        savedTask.setCompleted(false);

        when(taskRepository.save(any(Task.class)))
                .thenReturn(savedTask);

        TaskResponse result = taskService.createTask(request);

        assertEquals(1L, result.getId());
        assertEquals("Apprendre JUnit", result.getTitle());
        assertEquals(false, result.isCompleted());

        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);

        verify(taskRepository).save(taskCaptor.capture());

        Task capturedTask = taskCaptor.getValue();

        assertEquals("Apprendre JUnit", capturedTask.getTitle());
        assertEquals(false, capturedTask.isCompleted());
    }

    @Test
    void updateTask_shouldUpdateTask_whenTaskExists() {

        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("Ancien titre");
        existingTask.setCompleted(false);

        TaskRequest request = new TaskRequest();
        request.setTitle("Nouveau titre");
        request.setCompleted(true);

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(existingTask));

        when(taskRepository.save(any(Task.class)))
                .thenReturn(existingTask);

        Optional<TaskResponse> result =
                taskService.updateTask(1L, request);

        assertTrue(result.isPresent());

        assertEquals(1L, result.get().getId());
        assertEquals("Nouveau titre", result.get().getTitle());
        assertTrue(result.get().isCompleted());

        verify(taskRepository).findById(1L);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void updateTask_shouldReturnEmpty_whenTaskDoesNotExist() {

        TaskRequest request = new TaskRequest();
        request.setTitle("Nouveau titre");
        request.setCompleted(true);

        when(taskRepository.findById(999L))
                .thenReturn(Optional.empty());

        Optional<TaskResponse> result =
                taskService.updateTask(999L, request);

        assertTrue(result.isEmpty());

        verify(taskRepository).findById(999L);

        verify(taskRepository, never())
                .save(any(Task.class));
    }

    @Test
    void deleteTask_shouldReturnTrue_whenTaskExists() {

        when(taskRepository.existsById(1L))
                .thenReturn(true);

        boolean result = taskService.deleteTask(1L);

        assertTrue(result);

        verify(taskRepository).existsById(1L);
        verify(taskRepository).deleteById(1L);
    }

    @Test
    void deleteTask_shouldReturnFalse_whenTaskDoesNotExist() {

        when(taskRepository.existsById(999L))
                .thenReturn(false);

        boolean result = taskService.deleteTask(999L);

        assertFalse(result);

        verify(taskRepository).existsById(999L);

        verify(taskRepository, never())
                .deleteById(999L);
    }
}