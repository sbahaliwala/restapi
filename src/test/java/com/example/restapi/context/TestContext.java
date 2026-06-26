package com.example.restapi.context;

import io.restassured.response.Response;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@Component
@Scope("cucumber-glue")
public class TestContext {
    private Response response;
    private String savedObjectId;
    private Object savedPayload;
}
