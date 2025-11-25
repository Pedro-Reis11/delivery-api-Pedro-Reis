package com.delivery_api.Projeto.Delivery.API.swagger;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SwaggerIntegrationTest {

    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void testSwaggerUIAccessible() {
        String url = "http://localhost:" + port + "/swagger-ui.html";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("swagger"));
    }

    @Test
    public void testApiDocsAccessible() {
        String url = "http://localhost:" + port + "/api-docs";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("openapi"));
        assertTrue(response.getBody().contains("DeliveryTech API"));
    }

    @Test
    public void testApiDocsContainsExpectedEndpoints() {
        String url = "http://localhost:" + port + "/api-docs";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        String body = response.getBody();
        assertTrue(body.contains("/restaurantes"));
        assertTrue(body.contains("/produtos"));
        assertTrue(body.contains("/pedidos"));
        assertTrue(body.contains("/auth"));
    }
}
