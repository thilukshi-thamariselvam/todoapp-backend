package com.example.todoapp.entity;

import com.example.todoapp.enums.Priority;
import com.example.todoapp.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "todo", indexes = {
        @Index(name = "idx_due_date", columnList = "dueDate"),
        @Index(name = "idx_priority", columnList = "priority")
})

public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String title;
    private String description;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String listCategory;

    private boolean isPinned;

    @Column(name = "reminder_time")
    private LocalDateTime reminder;

    @ElementCollection
    @CollectionTable(name = "todo_tags", joinColumns = @JoinColumn(name = "todo_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();

    @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubTask> subTasks = new ArrayList<>();

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
        subTask.setTodo(this);
    }
}