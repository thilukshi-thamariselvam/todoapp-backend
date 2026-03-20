package com.example.todoapp.dto;

import com.example.todoapp.enums.Priority;
import com.example.todoapp.enums.Status;
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

    private LocalDateTime dueDate;

    private Priority priority;

    private Status status;

    private String listCategory;

    private boolean isPinned;

    private LocalDateTime reminder;

    private List<String> tags;

    private List<SubTaskDTO> subTasks;
}
