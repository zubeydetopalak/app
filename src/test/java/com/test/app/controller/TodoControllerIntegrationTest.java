package com.test.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.app.config.TestMockConfig;
import com.test.app.model.Todo;
import com.test.app.service.TodoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestMockConfig.class)
@WebMvcTest(TodoController.class)
class TodoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TodoService todoService;

    @Test
    void testCreateAndGetTodo() throws Exception {
        Todo todo = new Todo(null, "Test title", "Test desc", false);
        Todo saved = new Todo(1L, "Test title", "Test desc", false);
        when(todoService.createTodo(any(Todo.class))).thenReturn(saved);
        when(todoService.getAllTodos()).thenReturn(Arrays.asList(saved));
        // Create
        ResultActions createResult = mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todo)));
        createResult.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test title"));
        // Get all
        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testGetTodoById_NotFound() throws Exception {
        when(todoService.getTodoById(99999L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/todos/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateTodo() throws Exception {
        Todo todo = new Todo(null, "ToUpdate", "desc", false);
        Todo created = new Todo(2L, "ToUpdate", "desc", false);
        Todo updated = new Todo(2L, "Updated", "desc", false);
        when(todoService.createTodo(any(Todo.class))).thenReturn(created);
        when(todoService.updateTodo(any(Long.class), any(Todo.class))).thenReturn(Optional.of(updated));
        String content = objectMapper.writeValueAsString(todo);
        String response = mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Todo createdFromResponse = objectMapper.readValue(response, Todo.class);
        createdFromResponse.setTitle("Updated");
        mockMvc.perform(put("/api/todos/" + createdFromResponse.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createdFromResponse)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"));
    }

    @Test
    void testDeleteTodo() throws Exception {
        Todo created = new Todo(3L, "ToDelete", "desc", false);
        when(todoService.createTodo(any(Todo.class))).thenReturn(created);
        when(todoService.deleteTodo(3L)).thenReturn(true).thenReturn(false);
        String content = objectMapper.writeValueAsString(new Todo(null, "ToDelete", "desc", false));
        String response = mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Todo createdFromResponse = objectMapper.readValue(response, Todo.class);
        // Sil
        mockMvc.perform(delete("/api/todos/" + createdFromResponse.getId()))
                .andExpect(status().isNoContent());
        // Tekrar silmeye çalışınca not found
        mockMvc.perform(delete("/api/todos/" + createdFromResponse.getId()))
                .andExpect(status().isNotFound());
    }
}
