package com.example.restapi.steps;

import com.example.restapi.client.ApiClient;
import com.example.restapi.context.TestContext;
import io.cucumber.java.After;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class Hooks {

    @Autowired
    private TestContext context;

    @Autowired
    private ApiClient apiClient;

    @After("@Sample")
    public void cleanUpOrphanedResources() {
        if (context.getSavedObjectId() != null) {
            
            // cleanup only if an object was left behind
            log.debug("Cleaning up orphaned object with ID: {}", context.getSavedObjectId());

            Response response = apiClient.execute("DELETE", "/" + context.getSavedObjectId());
            context.setResponse(response);

            if (response.statusCode() == 200) {
                log.debug("Orphaned object with ID {} deleted successfully", context.getSavedObjectId());
            } else {
                log.error("Failed to delete orphaned object with ID {}", context.getSavedObjectId());
            }
        }
    }
}
