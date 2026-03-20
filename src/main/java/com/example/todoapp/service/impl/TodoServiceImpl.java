package com.example.todoapp.service.impl;

import com.example.todoapp.dto.SubTaskDTO;
import com.example.todoapp.dto.TodoRequestDTO;
import com.example.todoapp.entity.SubTask;
import com.example.todoapp.entity.Todo;
import com.example.todoapp.enums.Status;
import com.example.todoapp.repository.TodoRepository;
import com.example.todoapp.service.TodoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public List<Todo> getAllTodos() {
        return todoRepository.findAllByOrderByIsPinnedDescDueDateAsc();
    }

    @Override
    public Todo createTodo(TodoRequestDTO dto) {
        Todo todo = new Todo();
        mapDtoToEntity(dto, todo);

        if (todo.getStatus() == null) todo.setStatus(Status.PENDING);

        return todoRepository.save(todo);
    }

    @Override
    public Todo updateTodo(String id, TodoRequestDTO dto) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));

        mapDtoToEntity(dto, todo);
        return todoRepository.save(todo);
    }

    @Override
    public void deleteTodo(String id) {
        todoRepository.deleteById(id);
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
            todo.getSubTasks().clear(); // Clear old for simplicity (or merge logic)
            for (SubTaskDTO subDto : dto.getSubTasks()) {
                SubTask subTask = new SubTask(subDto.getTitle(), subDto.isCompleted());
                todo.addSubTask(subTask);
            }
        }
    }
}
