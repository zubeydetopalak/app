package com.test.app.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.test.app.model.Todo;
import com.test.app.repository.TodoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class BirimTestler {

    @Mock
    TodoRepository repository;

    @InjectMocks
    TodoService service;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTodo(){
        Todo todo = new Todo(null,"asd","asd",true);
        Todo created = new Todo(1L,"asd","asd",true);
        when(repository.save(todo)).thenReturn(created);
        Todo dbTodo = service.createTodo(todo);
        Assertions.assertEquals(1L, dbTodo.getId());
    }

    @Test
    public void deleteTodo(){
        Todo todo = new Todo(1L,"asd","asd",true);
        when(repository.findById(1L)).thenReturn(Optional.of(todo));
        doNothing().when(repository).deleteById(1L);
        boolean b = service.deleteTodo(1L);
        Assertions.assertTrue(b);
    }
    @Test
    public void updatedTodo(){
        Todo toUpdate=new Todo(1L,"toUpdate","fdfds",true);
        Todo updated=new Todo(1L,"updated","klf",true);
        when(repository.findById(1L)).thenReturn(Optional.of(toUpdate));
        when(repository.save(updated)).thenReturn(updated);
       Optional<Todo> db=service.updateTodo(1L,updated);
       Assertions.assertTrue(db.isPresent());
       Assertions.assertEquals("updated",db.get().getTitle());
    }
    @Test
    public void getTodoById(){
        Todo todo=new Todo(1L,"Test","Ders",true);
        when(repository.findById(1L)).thenReturn(Optional.of(todo));
        Optional<Todo> todoById = service.getTodoById(1L);
        Assertions.assertEquals(1L,todoById.get().getId());
        Assertions.assertEquals("Test",todoById.get().getTitle());
    }

    @Test
    public void getAllTodos(){
        Todo todo = new Todo(1L, "test", "test", true);
        when(repository.findAll()).thenReturn(List.of(todo));
        List<Todo> allTodos = service.getAllTodos();
        Assertions.assertEquals(1,allTodos.size());
        Assertions.assertEquals("test", allTodos.get(0).getTitle());
    }

    @Test
    public void getByIdNotFound(){
        when(repository.findById(1L)).thenReturn(Optional.empty());
        Optional<Todo> todoById = service.getTodoById(1L);
        Assertions.assertTrue(todoById.isEmpty());
    }
    @Test
    public void deleteTodoNotFound(){
        when(repository.findById(1L)).thenReturn(Optional.empty());
        boolean b = service.deleteTodo(1L);
        Assertions.assertFalse(b);
    }
    @Test
    public void updateTodoNotFound(){
        when(repository.findById(1L)).thenReturn(Optional.empty());
        Optional<Todo> todo = service.updateTodo(1L, new Todo(1L, "FD", "FDSF", true));
        Assertions.assertTrue(todo.isEmpty());
    }


}
