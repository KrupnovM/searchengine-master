package searchengine.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Проверка загрузки домашней страницы.")
class DefaultControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DefaultController defaultController;
    @Test
    void index() {
        try {
            this.mockMvc.perform(get("/")).andReturn();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}