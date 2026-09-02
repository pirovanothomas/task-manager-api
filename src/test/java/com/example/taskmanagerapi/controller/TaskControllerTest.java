package com.example.taskmanagerapi.controller;

import com.example.taskmanagerapi.dto.TaskResponse;
import com.example.taskmanagerapi.service.TaskService;
import com.example.taskmanagerapi.dto.TaskRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    void getTasks_shouldReturn200() throws Exception {

        TaskResponse task1 =
                new TaskResponse(1L, "Apprendre Spring Boot", false);

        TaskResponse task2 =
                new TaskResponse(2L, "Apprendre Mockito", true);

        when(taskService.getAllTasks())
                .thenReturn(List.of(task1, task2));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                        .andExpect(jsonPath("$[0].id").value(1))
                        .andExpect(jsonPath("$[0].title")
                                .value("Apprendre Spring Boot"))
                        .andExpect(jsonPath("$[0].completed")
                                .value(false));
    }

    @Test
    void getTaskById_shouldReturn200_whenTaskExists() throws Exception {

        TaskResponse task =
                new TaskResponse(1L, "Apprendre Spring Boot", false);

        when(taskService.getTaskById(1L))
                .thenReturn(Optional.of(task));

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title")
                        .value("Apprendre Spring Boot"))
                .andExpect(jsonPath("$.completed")
                        .value(false));
    }

    @Test
    void getTaskById_shouldReturn404_whenTaskDoesNotExist() throws Exception {

        when(taskService.getTaskById(999L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tasks/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTask_shouldReturn201() throws Exception {

        TaskResponse createdTask =
                new TaskResponse(1L, "Apprendre Spring Boot", false);

        when(taskService.createTask(any(TaskRequest.class)))
                .thenReturn(createdTask);

        mockMvc.perform(post("/api/tasks")
                        .contentType("application/json")
                        .content("""
                            {
                                "title": "Apprendre Spring Boot",
                                "completed": false
                            }
                            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title")
                        .value("Apprendre Spring Boot"))
                .andExpect(jsonPath("$.completed")
                        .value(false))
                .andExpect(header().string(
                        "Location",
                        "http://localhost/api/tasks/1"
                ));

        verify(taskService).createTask(any(TaskRequest.class));
    }

    @Test
    void createTask_shouldReturn400_whenTitleIsInvalid() throws Exception {

        mockMvc.perform(post("/api/tasks")
                        .contentType("application/json")
                        .content("""
                            {
                                "title": "AB",
                                "completed": false
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Le titre doit contenir entre 3 et 100 caractères"));
    }

    @Test
    void createTask_shouldReturn400_whenTitleIsBlank() throws Exception {

        mockMvc.perform(post("/api/tasks")
                        .contentType("application/json")
                        .content("""
                            {
                                "title": "",
                                "completed": false
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Le titre est obligatoire"));
    }

    @Test
    void createTask_shouldReturn400_whenTitleIsTooShort() throws Exception {

        mockMvc.perform(post("/api/tasks")
                        .contentType("application/json")
                        .content("""
                            {
                                "title": "AB",
                                "completed": false
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error")
                        .value("Validation failed"))
                .andExpect(jsonPath("$.message")
                        .value("Le titre doit contenir entre 3 et 100 caractères"));
    }

    @Test
    void updateTask_shouldReturn200_whenTaskExists() throws Exception {

        TaskResponse updatedTask =
                new TaskResponse(1L, "Apprendre Spring Boot avancé", true);

        when(taskService.updateTask(
                any(Long.class),
                any(TaskRequest.class)
        )).thenReturn(Optional.of(updatedTask));

        mockMvc.perform(put("/api/tasks/1")
                        .contentType("application/json")
                        .content("""
                            {
                                "title": "Apprendre Spring Boot avancé",
                                "completed": true
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title")
                        .value("Apprendre Spring Boot avancé"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void updateTask_shouldReturn404_whenTaskDoesNotExist() throws Exception {

        when(taskService.updateTask(
                any(Long.class),
                any(TaskRequest.class)
        )).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/tasks/999")
                        .contentType("application/json")
                        .content("""
                            {
                                "title": "Tâche inexistante",
                                "completed": false
                            }
                            """))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateTask_shouldReturn400_whenTitleIsTooShort() throws Exception {

        mockMvc.perform(put("/api/tasks/1")
                        .contentType("application/json")
                        .content("""
                            {
                                "title": "AB",
                                "completed": false
                            }
                            """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteTask_shouldReturn204_whenTaskExists() throws Exception {

        when(taskService.deleteTask(1L))
                .thenReturn(true);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTask_shouldReturn404_whenTaskDoesNotExist() throws Exception {

        when(taskService.deleteTask(999L))
                .thenReturn(false);

        mockMvc.perform(delete("/api/tasks/999"))
                .andExpect(status().isNotFound());
    }
}