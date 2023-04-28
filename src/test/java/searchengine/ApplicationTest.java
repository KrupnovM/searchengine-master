package searchengine;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("Проверка запуска приложения.")
public class ApplicationTest {
    @Test
    void main() {
        SpringApplication.run(Application.class);
    }
}
