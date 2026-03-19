package com.example.todoapp.service;

import com.example.todoapp.entity.Todo;

import java.util.List;

public interface TodoService {

    List<Todo> getAllTodos();

    Todo createTodo(Todo todo);

    Todo updateTodo(String id, Todo todo);

    void deleteTodo(String id);
}
