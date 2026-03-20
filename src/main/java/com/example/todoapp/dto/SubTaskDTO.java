package com.example.todoapp.dto;

import lombok.Data;

@Data
public class SubTaskDTO {
    private Long id;
    private String title;
    private boolean isCompleted;
}
