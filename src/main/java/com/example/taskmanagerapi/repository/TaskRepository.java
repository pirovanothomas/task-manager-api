package com.example.taskmanagerapi.repository;


import com.example.taskmanagerapi.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

        }