package com.test.app.config;

import com.test.app.service.TodoService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestMockConfig {
    @Bean
    public TodoService todoService() {
        return Mockito.mock(TodoService.class);
    }
}

