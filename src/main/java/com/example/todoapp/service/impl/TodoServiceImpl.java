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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Predicate;

@Slf4j
@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public Page<Todo> getAllTodos(String priority, String status, String search, Pageable pageable) {
        Specification<Todo> spec = (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isTrue(root.get("active")));
            if (priority != null && !priority.isEmpty()) predicates.add(cb.equal(root.get("priority"), priority));
            if (status != null && !status.isEmpty()) predicates.add(cb.equal(root.get("status"), status));
            if (search != null && !search.isEmpty()) {
                String searchPattern = "%" + search.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("title")), searchPattern),
                        cb.like(cb.lower(root.get("description")), searchPattern)
                ));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Sort pinnedSort = Sort.by(Sort.Order.desc("pinned"));
        Sort defaultSort = Sort.by(Sort.Direction.DESC, "createdDate");
        Sort userSort = pageable.getSort();

        Sort finalSort;
        if (userSort.isUnsorted()) {
            finalSort = pinnedSort.and(defaultSort).and(Sort.by(Sort.Direction.DESC, "updatedDate"));
        } else {
            finalSort = pinnedSort.and(userSort).and(Sort.by(Sort.Direction.DESC, "updatedDate"));
        }

        Pageable customPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), finalSort);

        return todoRepository.findAll(spec, customPageable);
    }

    @Override
    public Todo createTodo(TodoRequestDTO dto) {
        log.info("Creating new todo with title: {}", dto.getTitle());
        Todo todo = new Todo();
        mapDtoToEntity(dto, todo);

        if (todo.getStatus() == null) todo.setStatus(Status.PENDING);
        todo.setActive(true);

        Todo saved = todoRepository.save(todo);
        log.info("Todo created successfully with ID: {}", saved.getId());
        return saved;
    }

    @Override
    public Todo updateTodo(String id, TodoRequestDTO dto) {
        log.info("Updating todo with ID: {}", id);

        Todo todo = todoRepository.findByIdAndActiveTrue(id)
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
        log.info("Soft deleting todo with ID: {}", id);

        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cannot delete. Todo not found with ID: {}", id);
                    return new ResourceNotFoundException("Todo not found with id: " + id);
                });

        todo.setActive(false);
        todoRepository.save(todo);

        log.info("Todo soft deleted successfully");
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
