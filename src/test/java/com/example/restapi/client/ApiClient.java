package com.example.restapi.client;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ApiClient {

    @Value("${server.url}")
    private String baseURI;

    @Value("${server.api-key}")
    private String apiKey;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    /**
     * Core wrapper method to execute HTTP requests with conditional authentication
     * based on active profile.
     */
    public Response execute(String method, String path, Object body) {
        log.debug("Executing {} request for path: {}", method, path);
        // 1. Initialize the base request specification
        RequestSpecification request = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);

        // Add the API key header only if the active profile is "test"
        if (activeProfile.equals("test")) {
            request.header("x-api-key", apiKey);
            log.debug("API Key: {}", apiKey);
            log.debug("Active Profile: {}", activeProfile);
        }

        // 3. Attach payload if present
        if (body != null) {
            request.body(body);
        }

        // 4. Execute and return the response based on method type
        return switch (method.toUpperCase()) {
            case "POST" -> request.post(path);
            case "GET" -> request.get(path);
            case "DELETE" -> request.delete(path);
            case "PUT" -> request.put(path);
            case "PATCH" -> request.patch(path);
            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        };
    }

    // Overloaded convenience method for requests without a body (like GET/DELETE)
    public Response execute(String method, String path) {
        return execute(method, path, null);
    }

    public void configureApi() {
        RestAssured.baseURI = baseURI;
        log.debug("Base URL set to :{}", RestAssured.baseURI);
    }
}