package com.test.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.app.config.Configuration;
import com.test.app.model.Todo;
import com.test.app.repository.TodoRepository;
import com.test.app.service.TodoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(Configuration.class)
@WebMvcTest(TodoController.class)
public class IntegrationTest {

     @Autowired
     public ObjectMapper mapper;

     @Autowired
    public MockMvc mockMvc;

     @Autowired
    public TodoService service;

    @Test
    public void createTodoAndGetTodo() throws Exception {
        Todo todo=new Todo(null,"Test","Ders",true);
        Todo saved=new Todo(1L,"Test","Ders",true);
        when(service.createTodo(any(Todo.class))).thenReturn(saved);
        when(service.getTodoById(any(Long.class))).thenReturn(Optional.of(saved)) ;
    mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(todo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test"));
    mockMvc.perform(get("/api/todos/1"))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("Test"));
    }

    @Test
    public void deleteTodo() throws Exception {
        Todo todo=new Todo(null,"Yazılım","ders",false);
        Todo saved=new Todo(2L,"Yazılım","ders",false);
        when(service.createTodo(any(Todo.class))).thenReturn(saved);
        String contentAsString = mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(todo)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Todo created=mapper.readValue(contentAsString, Todo.class);


        when(service.deleteTodo(any(Long.class))).thenReturn(true).thenReturn(false);
        mockMvc.perform(delete("/api/todos/"+created.getId()))
                .andExpect(status().isNoContent());
        mockMvc.perform(delete("/api/todos/" + created.getId()))
                .andExpect(status().isNotFound());
        Assertions.assertEquals(saved.getId(), created.getId());
    }
    @Test
    public void updateTodo() throws Exception {

        Todo toUpdate=new Todo(1L,"ToUpdate","Test",true);
        Todo update=new Todo(1L,"Updated","Deneme",true);

        when(service.updateTodo(any(Long.class),any(Todo.class))).thenReturn(Optional.of(update));
        mockMvc.perform(put("/api/todos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"));
    }

}
