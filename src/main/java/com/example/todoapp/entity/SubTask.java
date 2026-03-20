package com.example.todoapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubTask {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private boolean isCompleted;

    @ManyToOne
    @JoinColumn (name = "todo_id")
    @JsonIgnore
    private Todo todo;

    public SubTask (String title, boolean isCompleted) {
        this.title = title;
        this.isCompleted = isCompleted;
    }
}
