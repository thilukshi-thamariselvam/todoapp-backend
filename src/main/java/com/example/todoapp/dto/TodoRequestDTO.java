package com.example.todoapp.dto;

import com.example.todoapp.enums.Priority;
import com.example.todoapp.enums.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TodoRequestDTO {
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @Size(max = 500)
    private String description;

    @FutureOrPresent(message = "Due date must be today or in the future")
    private LocalDateTime dueDate;

    private Priority priority;

    private Status status;

    private String listCategory;

    @JsonProperty("isPinned")
    private boolean pinned;

    private LocalDateTime reminder;

    private List<String> tags;

    private List<SubTaskDTO> subTasks;
}
