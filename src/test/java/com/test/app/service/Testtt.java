package com.test.app.service;

import com.test.app.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

public class Testtt {


    TodoService service=new TodoService(new TodoRepository());

    @Test
    public void testet(){
        service.deleteTodo(1L);

    }
}
