package com.example.todoapp.repository;

import com.example.todoapp.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, String> {
    @Query(value = "SELECT * FROM todo t ORDER BY t.is_pinned DESC, t.due_date ASC", nativeQuery = true)
    List<Todo> findAllSorted();
}