package com.test.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.app.AppApplication;
import com.test.app.config.TestConfig;
import com.test.app.model.Todo;
import com.test.app.service.TodoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestConfig.class)
@WebMvcTest(TodoController.class)
public class Integration {

    @Autowired
    TodoService service;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void createTodo() throws Exception {
        Todo todo=new Todo(null,"Test","Title",false);
        Todo saved=new Todo(1L,"Test","Title",false);
        when(service.createTodo(any(Todo.class))).thenReturn(saved);
        when(service.getAllTodos()).thenReturn(List.of(saved));
        mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(todo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test"));
        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testGetTodoById_NotFound() throws Exception {
        when(service.getTodoById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/todos/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUptdateTodo() throws Exception {
        Todo updated=new Todo(1L,"updated","",true);
        when(service.updateTodo(any(Long.class), any(Todo.class))).thenReturn(Optional.of(updated));
        String content=mapper.writeValueAsString(updated);
        mockMvc.perform(put("/api/todos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("updated"))
                .andExpect(jsonPath("$.id").value(1L));

    }

    @Test
    public void testDeleteTodo() throws Exception {
        when(service.deleteTodo(any(Long.class))).thenReturn(true).thenReturn(false);

        mockMvc.perform(delete("/api/todos/" + 1L))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/todos/" + 1L))
                .andExpect(status().isNotFound());

    }

}
