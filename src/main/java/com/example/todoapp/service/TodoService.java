package com.example.todoapp.service;

import com.example.todoapp.dto.TodoRequestDTO;
import com.example.todoapp.entity.Todo;

import java.util.List;

public interface TodoService {

    List<Todo> getAllTodos();

    Todo createTodo(TodoRequestDTO dto);

    Todo updateTodo(String id, TodoRequestDTO dto);

    void deleteTodo(String id);
}