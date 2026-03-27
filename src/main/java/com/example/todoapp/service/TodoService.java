package com.example.todoapp.service;

import com.example.todoapp.dto.TodoRequestDTO;
import com.example.todoapp.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TodoService {

    Page<Todo> getAllTodos(String priority, String status, String search, Pageable pageable);

    Todo createTodo(TodoRequestDTO dto);

    Todo updateTodo(String id, TodoRequestDTO dto);

    void deleteTodo(String id);

    Todo getTodoById(String id);
}