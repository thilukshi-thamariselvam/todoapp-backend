package com.example.todoapp.controller;

import com.example.todoapp.entity.Todo;
import com.example.todoapp.service.TodoService;
import com.example.todoapp.util.CommonResponse;
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

        return new CommonResponse<>(
                true,
                "Todos fetched successfully",
                todos
        );
    }

    @PostMapping("/")
    public CommonResponse<Todo> addTodo(@RequestBody Todo todo) {
        Todo saved = todoService.createTodo(todo);

        return new CommonResponse<>(
                true,
                "Todo created successfully",
                saved
        );
    }

    @PutMapping("/{id}")
    public CommonResponse<Todo> updateTodo(@PathVariable String id, @RequestBody Todo todo) {
        Todo updated = todoService.updateTodo(id, todo);

        return new CommonResponse<>(
                true,
                "Todo Updated successfully",
                updated
        );
    }

    @DeleteMapping("/{id}")
    public CommonResponse<Void> deleteTodo(@PathVariable String id) {
        todoService.deleteTodo(id);

        return new CommonResponse<>(
                true,
                "Todo deleted Successfully",
                null
        );
    }


}
