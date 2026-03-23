package com.example.todoapp.service.impl;

import com.example.todoapp.dto.SubTaskDTO;
import com.example.todoapp.dto.TodoRequestDTO;
import com.example.todoapp.entity.SubTask;
import com.example.todoapp.entity.Todo;
import com.example.todoapp.enums.Status;
import com.example.todoapp.repository.TodoRepository;
import com.example.todoapp.service.TodoService;
import com.example.todoapp.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public List<Todo> getAllTodos() {
        log.info("Fetching all todos");
        return todoRepository.findAllSorted();
    }

    @Override
    public Todo createTodo(TodoRequestDTO dto) {
        log.info("Creating new todo with title: {}", dto.getTitle());
        Todo todo = new Todo();
        mapDtoToEntity(dto, todo);

        if (todo.getStatus() == null) todo.setStatus(Status.PENDING);

        Todo saved = todoRepository.save(todo);
        log.info("Todo created successfully with ID: {}", saved.getId());
        return saved;
    }

    @Override
    public Todo updateTodo(String id, TodoRequestDTO dto) {
        log.info("Updating todo with ID: {}", id);

        Todo todo = todoRepository.findById(id)
                        .orElseThrow(() -> {
                            log.error("Todo not found with ID: {}", id);
                            return new ResourceNotFoundException("Todo not found with id: " + id);
                        });

        mapDtoToEntity(dto, todo);
        Todo updated = todoRepository.save(todo);
        log.info("Todo updated successfully");
        return updated;
    }

    @Override
    public void deleteTodo(String id) {
        log.info("Deleting todo with ID: {}", id);

        if (!todoRepository.existsById(id)) {
            log.error("Cannot delete. Todo not found with ID: {}", id);
            throw new ResourceNotFoundException("Todo not found with id: " + id);
        }
        todoRepository.deleteById(id);
        log.info("Todo deleted successfully");
    }

    private void mapDtoToEntity(TodoRequestDTO dto, Todo todo) {
        todo.setTitle(dto.getTitle());
        todo.setDescription(dto.getDescription());
        todo.setDueDate(dto.getDueDate());
        todo.setPriority(dto.getPriority());
        todo.setStatus(dto.getStatus());
        todo.setListCategory(dto.getListCategory());
        todo.setPinned(dto.isPinned());
        todo.setReminder(dto.getReminder());

        if (dto.getTags() != null) {
            todo.getTags().clear();
            todo.getTags().addAll(dto.getTags());
        }

        if (dto.getSubTasks() != null) {
            todo.getSubTasks().clear();
            for (SubTaskDTO subDto : dto.getSubTasks()) {
                SubTask subTask = new SubTask(subDto.getTitle(), subDto.isCompleted());
                todo.addSubTask(subTask);
            }
        }
    }
}
