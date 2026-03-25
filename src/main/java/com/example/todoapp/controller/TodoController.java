package com.example.todoapp.controller;

import com.example.todoapp.dto.TodoRequestDTO;
import com.example.todoapp.entity.Todo;
import com.example.todoapp.service.TodoService;
import com.example.todoapp.util.CommonResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public CommonResponse<Page<Todo>> getTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "dueDate") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String status
    ) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        Page<Todo> todosPage = todoService.getAllTodos(priority, status, pageable);

        return new CommonResponse<>(true, "Todos fetched successfully", todosPage);
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