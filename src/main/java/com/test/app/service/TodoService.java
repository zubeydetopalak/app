package com.test.app.service;

import com.test.app.model.Todo;
import com.test.app.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    public Optional<Todo> getTodoById(Long id) {
        return todoRepository.findById(id);
    }

    public Todo createTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    public Optional<Todo> updateTodo(Long id, Todo updated) {
        return todoRepository.findById(id).map(e -> {
            e.setTitle(updated.getTitle());
            e.setDescription(updated.getDescription());
            e.setCompleted(updated.isCompleted());
            return todoRepository.save(e);
        });
    }

    public boolean deleteTodo(Long id) {
        if (todoRepository.findById(id).isPresent()) {
            todoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

