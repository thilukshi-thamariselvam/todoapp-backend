package com.example.todoapp.controller;

import com.example.todoapp.dto.TodoRequestDTO;
import com.example.todoapp.entity.Todo;
import com.example.todoapp.service.TodoService;
import com.example.todoapp.util.CommonResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search
    ) {
        log.info("Request received to fetch todos - Page: {}, Size: {}, Sort: {}, Direction: {}, Priority: {}, Status: {}, Search: {}",
                page, size, sort, direction, priority, status, search);

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        Page<Todo> todosPage = todoService.getAllTodos(priority, status, search, pageable);

        log.info("Successfully fetched {} todos", todosPage.getTotalElements());
        return new CommonResponse<>(true, "Todos fetched successfully", todosPage);
    }

    @GetMapping("/{id}")
    public CommonResponse<Todo> getTodoById(@PathVariable String id) {
        log.info("Request received to fetch todo by ID: {}", id);

        Todo todo = todoService.getTodoById(id);

        log.info("Successfully fetched todo ID: {}", id);
        return new CommonResponse<>(true, "Todo fetched successfully", todo);
    }

    @PostMapping("/")
    public CommonResponse<Todo> addTodo(@Valid @RequestBody TodoRequestDTO dto) {
        log.info("Request received to create todo: {}", dto.getTitle());

        Todo saved = todoService.createTodo(dto);

        log.info("Todo created successfully with ID: {}", saved.getId());
        return new CommonResponse<>(true, "Todo created successfully", saved);
    }

    @PutMapping("/{id}")
    public CommonResponse<Todo> updateTodo(@PathVariable String id, @Valid @RequestBody TodoRequestDTO dto) {
        log.info("Request received to update todo ID: {}", id);

        Todo updated = todoService.updateTodo(id, dto);

        log.info("Todo ID: {} updated successfully", id);
        return new CommonResponse<>(true, "Todo Updated successfully", updated);
    }

    @DeleteMapping("/{id}")
    public CommonResponse<Void> deleteTodo(@PathVariable String id) {
        log.info("Request received to delete todo ID: {}", id);

        todoService.deleteTodo(id);

        log.info("Todo ID: {} deleted successfully", id);
        return new CommonResponse<>(true, "Todo deleted Successfully", null);
    }
}