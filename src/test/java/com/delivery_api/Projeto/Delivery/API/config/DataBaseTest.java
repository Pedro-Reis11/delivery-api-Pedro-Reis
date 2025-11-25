package com.delivery_api.Projeto.Delivery.API.config;
import com.delivery_api.Projeto.Delivery.API.controller.TestDataConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestDataConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Testes de banco de dados")
public class DataBaseTest {

    @Autowired
    private DataSource dataSource;

    @Test
    @DisplayName("Verificar URL do banco de dados")
    void should_UseH2Database() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            String url = conn.getMetaData().getURL();
            System.out.println("Database URL: " + url);
            assertTrue(url.contains("jdbc:h2:mem"));
        }
    }
}
