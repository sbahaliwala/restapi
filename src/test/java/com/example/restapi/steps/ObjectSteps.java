package com.example.restapi.steps;

import com.example.restapi.client.ApiClient;
import com.example.restapi.context.TestContext;
import com.example.restapi.models.ObjectRequest;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@Slf4j
public class ObjectSteps {

    @Autowired
    private ApiClient apiClient;

    @Autowired
    private TestContext context;

    public void tearDown() {
        deleteObject();
    }

    @Given("the API client is authenticated and ready")
    public void configureApi() {
        apiClient.configureApi();
    }

    @When("I query all objects")
    public void verifyRetrieveAll() {
        Response response = apiClient.execute("GET", "/");
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
        var response = apiClient.execute("GET", "/" + context.getSavedObjectId());
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
        var response = apiClient.execute("DELETE", "/" + context.getSavedObjectId());
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
        var response = apiClient.execute("GET", "/" + invalidId);
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

    @Given("a {string} item is created")
    public void createObjectAttribute(String s) {
        createObjectWithAttribute("name", s);
    }

    @Given("the CPU model is {string}")
    public void the_CPU_model_is(String s) {
        createObjectWithAttribute("CPU model", s);
    }

    @Given("has a price of {string}")
    public void has_a_price_of(String s) {
        createObjectWithAttribute("price", s);
    }

    @When("the request to add the item is made")
    public void the_request_to_add_the_item_is_made() {
        createObject(context.getNewObject());
    }

    @Then("a {string} is created")
    public void a_is_created(String s) {
        JsonPath jsonPath = context.getResponse().jsonPath();
        assertThat(jsonPath.getString("id"), equalTo(context.getSavedObjectId()));
        assertThat(jsonPath.getString("name"), equalTo(s));
    }

    private void createObject(Map<String, String> dataMap) {
        Map<String, Object> attributes = new HashMap<>();
        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
            // Skip the name field, as it is handled separately
            if ("name".equals(entry.getKey())) {
                continue;
            }
            // Extract the key and value
            String key = entry.getKey();
            String value = entry.getValue();
            // Add the key-value pair to the attributes map
            if (key.equals("price")) {
                attributes.put(key, Double.parseDouble(value));
            }
            if (key.equals("year")) {
                attributes.put(key, Integer.parseInt(value));
            } else {
                attributes.put(key, value);
            }
        }

        ObjectRequest requestPayload = ObjectRequest.builder()
                .name(dataMap.get("name"))
                .data(attributes)
                .build();
        log.debug("Request payload {}:", requestPayload);

        // POST request with API key and JSON body
        var response = apiClient.execute("POST", "/", requestPayload);

        log.debug("Response Status Code : {}", response.getStatusCode());
        log.debug("Response Body : {}", response.getBody().asString());
        context.setResponse(response);

        if (response.getStatusCode() == 200) {
            String extractedId = response.jsonPath().getString("id");
            context.setSavedObjectId(extractedId);
            context.setNewObject(null);
        }
    }

    private void createObjectWithAttribute(String attributeKey, String attributeValue) {
        if (context.getNewObject() == null) {
            context.setNewObject(new HashMap<>());
        }
        Map<String, String> dataMap = context.getNewObject();
        dataMap.put(attributeKey, attributeValue);
        context.setNewObject(dataMap);
    }
}
