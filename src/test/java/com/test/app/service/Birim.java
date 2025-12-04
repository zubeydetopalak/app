package com.test.app.service;

import com.test.app.model.Todo;
import com.test.app.repository.TodoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class Birim {

    @Mock
    private TodoRepository repository;

    @InjectMocks
    private TodoService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createTodo() {
        Todo todo = new Todo(null, "Test", "", true);
        Todo saved = new Todo(1L, "Test", "", true);
        when(repository.save(todo)).thenReturn(saved);
        Todo kayit = service.createTodo(todo);
        Assertions.assertEquals(1L, kayit.getId());
    }

    @Test
    public void getTodoByİd() {
        Todo todo = new Todo(1L, "asd", "asd", true);
        when(repository.findById(1L)).thenReturn(Optional.of(todo));
        Optional<Todo> dbTodo = service.getTodoById(1L);
        Assertions.assertTrue(dbTodo.isPresent());
        Assertions.assertEquals(1L, dbTodo.get().getId());
    }
    @Test
    public void deleteById(){
        Todo find=new Todo(1L,"silinecek","",false);
        when(repository.findById(1L)).thenReturn(Optional.of(find));
        doNothing().when(repository).deleteById(1L);
        boolean b = service.deleteTodo(1L);
        Assertions.assertTrue(b);
    }

    @Test
    public void updateTodo(){
        Todo todo = new Todo(1L,"asd","asd",true);
        Todo updated = new Todo(1L,"Updated","asd",true);
        when(repository.findById(1L)).thenReturn(Optional.of(todo));
        when(repository.save(todo)).thenReturn(updated);

        Optional<Todo> todo1 = service.updateTodo(1L, updated);
        Assertions.assertTrue(todo1.isPresent());
        Assertions.assertEquals("Updated",todo1.get().getTitle());
    }

    @Test
    public void getAllTodos(){
        Todo todo=new Todo(1L,"Test","",true);
        when(repository.findAll()).thenReturn(Collections.singletonList(todo));
        List<Todo> allTodos = service.getAllTodos();
        Assertions.assertEquals(1,allTodos.size());
    }
    @Test
    public void getTodoGetId_NotFound(){
       when(repository.findById(999L)).thenReturn(Optional.empty());
        Optional<Todo> todoById = service.getTodoById(999L);
        Assertions.assertTrue(todoById.isEmpty());
    }

    @Test
    public void testUpdateTodoNotFound(){
        when(repository.findById(1L)).thenReturn(Optional.empty());
        Optional<Todo> todo = service.updateTodo(1L, new Todo(1L, "asd", "asd", true));
        Assertions.assertTrue(todo.isEmpty());
    }
   @Test
    public void deleteTodoNotFound(){
        when(repository.findById(1L)).thenReturn(Optional.empty());
       boolean b = service.deleteTodo(1L);
       Assertions.assertFalse(b);
   }

}
