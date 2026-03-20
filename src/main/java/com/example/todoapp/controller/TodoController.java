package com.example.todoapp.controller;

import com.example.todoapp.dto.TodoRequestDTO;
import com.example.todoapp.entity.Todo;
import com.example.todoapp.service.TodoService;
import com.example.todoapp.util.CommonResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "http://localhost:5173")
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/")
    public CommonResponse<List<Todo>> getTodos() {
        List<Todo> todos = todoService.getAllTodos();
        return new CommonResponse<>(true, "Todos fetched successfully", todos);
    }

    @PostMapping("/")
    public CommonResponse<Todo> addTodo(@Valid @RequestBody TodoRequestDTO dto) {
        Todo saved = todoService.createTodo(dto);
        return new CommonResponse<>(true, "Todo created successfully", saved);
    }

    @PutMapping("/{id}")
    public CommonResponse<Todo> updateTodo(@PathVariable String id, @Valid @RequestBody TodoRequestDTO dto) {
        Todo updated = todoService.updateTodo(id, dto);
        return new CommonResponse<>(true, "Todo Updated successfully", updated);
    }

    @DeleteMapping("/{id}")
    public CommonResponse<Void> deleteTodo(@PathVariable String id) {
        todoService.deleteTodo(id);
        return new CommonResponse<>(true, "Todo deleted Successfully", null);
    }
}