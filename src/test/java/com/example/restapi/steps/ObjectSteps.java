package com.example.restapi.steps;

import com.example.restapi.context.TestContext;
import com.example.restapi.models.ObjectRequest;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@Slf4j
public class ObjectSteps {

    @Value("${server.url}")
    private String baseURI;

    @Value("${server.api-key}")
    private String apiKey;

    @Autowired
    private TestContext context;

    Map<String, Object> myNewItem = new HashMap<>();

    @Given("the API client is authenticated and ready")
    public void configureApi() {
        RestAssured.baseURI = baseURI;
        log.debug("Base URL set to :{}", RestAssured.baseURI);
    }

    @When("I query all objects")
    public void verifyRetrieveAll() {
        var response = requestWithApiKey().get();
        context.setResponse(response);
        log.debug("Response{}", response.getBody().asString());
    }

    @Then("the response status code should be {int}")
    public void verifyStatusCode(int expectedStatus) {
        context.getResponse().then().statusCode(expectedStatus);
        log.debug("Status code :{}", expectedStatus);
        log.debug("Response{}", context.getResponse().getBody().asString());
    }

    @When("I create a new object with the following details:")
    public void createObject(DataTable dataTable) {
        Map<String, String> dataMap = dataTable.asMap(String.class, String.class);
        createObject(dataMap);
    }

    private void createObject(Map<String, String> dataMap) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("year", Integer.parseInt(dataMap.get("year")));
        attributes.put("price", Double.parseDouble(dataMap.get("price")));
        attributes.put("CPU model", dataMap.get("CPU model"));
        attributes.put("Hard disk size", dataMap.get("Hard disk size"));

        ObjectRequest requestPayload = ObjectRequest.builder()
                .name(dataMap.get("name"))
                .data(attributes)
                .build();
        log.debug("Request payload {}:", requestPayload);

        var response = requestWithApiKey()
                .body(requestPayload)
                .post();

        context.setResponse(response);
        log.debug("Response{}", response.getBody().asString());

        if (response.getStatusCode() == 200) {
            String extractedId = response.jsonPath().getString("id");
            context.setSavedObjectId(extractedId);
        }
    }

    @And("the response JSON should validate the creation details")
    public void validateCreationJson() {
        JsonPath jsonPath = context.getResponse().jsonPath();

        // JSON Path assertions satisfying Requirement #6
        assertThat(jsonPath.getString("id"), is(notNullValue()));
        assertThat(jsonPath.getString("name"), equalTo("Apple MacBook Ultra"));
        assertThat(jsonPath.getInt("data.year"), is(2026));
        assertThat(jsonPath.getDouble("data.price"), is(2849.99));
        assertThat(jsonPath.getString("data.'CPU model'"), equalTo("Intel Core i9"));
        assertThat(jsonPath.getString("data.'Hard disk size'"), equalTo("5 TB"));
    }

    @When("I retrieve the created object by its ID")
    public void retrieveObject() {
        var response = requestWithApiKey()
                .get("/" + context.getSavedObjectId());

        context.setResponse(response);
        log.debug("Response{}", response.getBody().asString());
    }

    @And("the fetched object details must match the created object")
    public void verifyFetchedDetails() {
        JsonPath jsonPath = context.getResponse().jsonPath();
        assertThat(jsonPath.getString("id"), equalTo(context.getSavedObjectId()));
        assertThat(jsonPath.getString("name"), equalTo("Apple MacBook Ultra"));
        assertThat(jsonPath.getInt("data.year"), is(2026));
        assertThat(jsonPath.getDouble("data.price"), is(2849.99));
        assertThat(jsonPath.getString("data.'CPU model'"), equalTo("Intel Core i9"));
        assertThat(jsonPath.getString("data.'Hard disk size'"), equalTo("5 TB"));
    }

    @When("I delete the created object")
    public void deleteObject() {
        var response = requestWithApiKey()
                .delete("/" + context.getSavedObjectId());

        context.setResponse(response);
        log.debug("Response{}", response.getBody().asString());
    }

    @And("the deletion message should confirm the object was deleted")
    public void verifyDeletionMessage() {
        JsonPath jsonPath = context.getResponse().jsonPath();
        assertThat(jsonPath.getString("message"), containsString("has been deleted"));
    }

    @When("I attempt to retrieve an object with ID {string}")
    public void getInvalidObject(String invalidId) {
        var response = requestWithApiKey()
                .get("/" + invalidId);
        context.setResponse(response);
        log.debug("Response{}", response.getBody().asString());
    }

    @And("the error message should contain {string}")
    public void verifyErrorMessage(String expectedError) {
        String errorPayload = context.getResponse().body().asString();
        assertThat(errorPayload.toLowerCase(), containsString(expectedError.toLowerCase()));
    }

    @Given("an object has been created and subsequently deleted")
    public void setupAndCleanupPreConditions() {
        // Step chaining to programmatically setup state
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("name", "Apple MacBook Pro 16");
        dataMap.put("year", "2019");
        dataMap.put("price", "1849.99");
        dataMap.put("CPU model", "Intel Core i9");
        dataMap.put("Hard disk size", "1 TB");
        createObject(dataMap);
        deleteObject();
    }

    @When("I attempt to delete the same object again")
    public void deleteSameObjectAgain() {
        deleteObject();
    }

    @And("the error response should contain an error message")
    public void verifyMissingResource() {
        JsonPath jsonPath = context.getResponse().jsonPath();
        assertThat(jsonPath.get("error"), is(notNullValue()));
    }

    private RequestSpecification requestWithApiKey() {
        return RestAssured.given()
                .header("x-api-key", apiKey)
                .contentType(ContentType.JSON);
    }
}
