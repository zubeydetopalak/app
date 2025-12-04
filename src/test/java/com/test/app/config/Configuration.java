package com.test.app.config;

import com.test.app.model.Todo;
import com.test.app.service.TodoService;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class Configuration {
    @Bean
    TodoService todoService(){
        return Mockito.mock(TodoService.class);
    }
}
