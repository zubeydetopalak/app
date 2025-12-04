package com.test.app.service;

import com.test.app.model.Todo;
import com.test.app.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoServiceTestt {
    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTodo() {
        Todo todo = new Todo(null, "title", "desc", false);
        Todo saved = new Todo(1L, "title", "desc", false);
        when(todoRepository.save(todo)).thenReturn(saved);
        Todo result = todoService.createTodo(todo);
        assertEquals(saved.getId(), result.getId());
    }

    @Test
    void testGetAllTodos() {
        when(todoRepository.findAll()).thenReturn(Collections.singletonList(new Todo(1L, "t", "d", false)));
        List<Todo> todos = todoService.getAllTodos();
        assertEquals(1L, todos.get(0).getId());
    }

    @Test
    void testGetTodoById() {
        Todo todo = new Todo(1L, "t", "d", false);
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        Optional<Todo> result = todoService.getTodoById(1L);
        assertTrue(result.isPresent());
        assertEquals(todo.getId(), result.get().getId());
    }

    @Test
    void testUpdateTodo() {
        Todo existing = new Todo(1L, "t", "d", false);
        Todo updated = new Todo(1L, "new", "newd", true);
        when(todoRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(todoRepository.save(any(Todo.class))).thenReturn(updated);
        Optional<Todo> result = todoService.updateTodo(1L, updated);
        assertTrue(result.isPresent());
        assertEquals("new", result.get().getTitle());
    }

    @Test
    void testDeleteTodo() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(new Todo(1L, "t", "d", false)));
        doNothing().when(todoRepository).deleteById(1L);
        boolean deleted = todoService.deleteTodo(1L);
        assertTrue(deleted);
    }

    @Test
    void testGetTodoById_NotFound() {
        when(todoRepository.findById(999L)).thenReturn(Optional.empty());
        Optional<Todo> result = todoService.getTodoById(999L);
        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateTodo_NotFound() {
        when(todoRepository.findById(999L)).thenReturn(Optional.empty());
        Todo updated = new Todo(999L, "fail", "fail", false);
        Optional<Todo> result = todoService.updateTodo(999L, updated);
        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteTodo_NotFound() {
        when(todoRepository.findById(999L)).thenReturn(Optional.empty());
        boolean deleted = todoService.deleteTodo(999L);
        assertFalse(deleted);
    }

    @Test
    void testGetTodoById_NotFound_ShouldFail() {
        when(todoRepository.findById(999L)).thenReturn(Optional.empty());
        Optional<Todo> result = todoService.getTodoById(999L);
        assertFalse(result.isPresent());
    }

    @Test
    void testUpdateTodo_NotFound_ShouldFail() {
        when(todoRepository.findById(999L)).thenReturn(Optional.empty());
        Todo updated = new Todo(999L, "fail", "fail", false);
        Optional<Todo> result = todoService.updateTodo(999L, updated);
        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteTodo_NotFound_ShouldFail() {
        when(todoRepository.findById(999L)).thenReturn(Optional.empty());
        boolean deleted = todoService.deleteTodo(999L);
        assertFalse(deleted);
    }

    @Test
    void testCreateTodoWithNullTitle_ShouldFail() {
        Todo todo = new Todo(null, null, "desc", false);
        when(todoRepository.save(todo)).thenReturn(todo);
        Todo result = todoService.createTodo(todo);
        assertNull(result.getTitle());
    }
}
