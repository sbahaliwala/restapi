package com.example.restapi.steps;

import com.example.restapi.context.TestContext;
import io.cucumber.java.After;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class Hooks {

    @Autowired
    private TestContext context;

    @After("@Sample")
    public void cleanUpOrphanedResources() {
        if (context.getSavedObjectId() != null) {
            // cleanup only if an object was left behind
            log.debug("Cleaning up orphaned object with ID: {}", context.getSavedObjectId());
            RestAssured.given()
                    .delete("/" + context.getSavedObjectId())
                    .then()
                    .log().ifValidationFails();
        }
    }
}
