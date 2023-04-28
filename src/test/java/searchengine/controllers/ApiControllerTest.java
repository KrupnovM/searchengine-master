package searchengine.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Тестирование API контроллера приложения.")
class ApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApiController apiController;

    @Test
    @DisplayName("Проверка получения статистики.")
    void statistics() {
        try {
            this.mockMvc.perform(get("/statistics").header("Authorization", "Basic YWRtaW46UXdlcnR5MjI="))
                    .andDo(print())
                    .andExpect(content().string(containsString("")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Проверка старта индексации сайтов.")
    void startIndexing() {
        try {
            this.mockMvc.perform(get("/startIndexing").header("Authorization", "Basic YWRtaW46UXdlcnR5MjI="))
                    .andDo(print())
                    .andExpect(content().string(containsString("")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Проверка остановки индексации сайтов.")
    void stopIndexing() {
        try {
            this.mockMvc.perform(get("/stopIndexing").header("Authorization", "Basic YWRtaW46UXdlcnR5MjI="))
                    .andDo(print())
                    .andExpect(content().string(containsString("")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Проверка поиска информации.")
    void searchWords() {
        try {
            this.mockMvc.perform(get("/api/search")
                            .header("Authorization", "Basic YWRtaW46UXdlcnR5MjI="))
                    .andDo(print())
                    .andExpect(content().string(containsString("")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Проверка индексации отдельного сайта.")
    void indexPage() {
        try {
            this.mockMvc.perform(post("/indexPage")
                            .param("url", "https://www.rambler.ru")
                            .header("Authorization", "Basic YWRtaW46UXdlcnR5MjI="))
                    .andDo(print())
                    .andExpect(content().string(containsString("")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
