package com.example.todoapp.service.impl;

import com.example.todoapp.dto.TodoRequestDTO;
import com.example.todoapp.entity.Todo;
import com.example.todoapp.enums.Priority;
import com.example.todoapp.enums.Status;
import com.example.todoapp.exception.ResourceNotFoundException;
import com.example.todoapp.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoServiceImpl todoService;

    private Todo todo;

    @BeforeEach
    void setUp() {
        todo = new Todo();
        todo.setId("test-uuid-123");
        todo.setTitle("Test Task");
        todo.setDescription("Test Description");
        todo.setActive(true);
        todo.setStatus(Status.PENDING);
    }

    @Test
    void testGetTodoById_Success() {
        when(todoRepository.findByIdAndActiveTrue("test-uuid-123")).thenReturn(Optional.of(todo));

        Todo result = todoService.getTodoById("test-uuid-123");

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());

        verify(todoRepository, times(1)).findByIdAndActiveTrue("test-uuid-123");
    }

    @Test
    void testGetTodoById_NotFound_ThrowsException() {
        when(todoRepository.findByIdAndActiveTrue("invalid-id")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            todoService.getTodoById("invalid-id");
        });
    }

    @Test
    void testCreateTodo_Success() {
        TodoRequestDTO dto = new TodoRequestDTO();
        dto.setTitle("New Task");
        dto.setPriority(Priority.HIGH);
        when(todoRepository.save(any(Todo.class))).thenAnswer(invocation -> {
            Todo savedTodo = invocation.getArgument(0);
            savedTodo.setId("generated-uuid-999");
            return savedTodo;
        });

        Todo result = todoService.createTodo(dto);

        assertNotNull(result);
        assertEquals("New Task", result.getTitle());
        assertEquals("generated-uuid-999", result.getId());
        assertEquals(Status.PENDING, result.getStatus());
        assertTrue(result.isActive());
    }

    @Test
    void testDeleteTodo_Success() {
        when(todoRepository.findById("test-uuid-123")).thenReturn(Optional.of(todo));
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);
        todoService.deleteTodo("test-uuid-123");

        assertFalse(todo.isActive(), "Todo should be marked as inactive (soft delete)");

        verify(todoRepository, times(1)).save(todo);
    }

    @Test
    void testUpdateTodo_Success() {
        TodoRequestDTO updateDto = new TodoRequestDTO();
        updateDto.setTitle("Updated Title");
        updateDto.setStatus(Status.COMPLETED);

        when(todoRepository.findByIdAndActiveTrue("test-uuid-123")).thenReturn(Optional.of(todo));

        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        Todo result = todoService.updateTodo("test-uuid-123", updateDto);

        assertEquals("Updated Title", result.getTitle());
        assertEquals(Status.COMPLETED, result.getStatus());
        verify(todoRepository, times(1)).save(todo);
    }
}


