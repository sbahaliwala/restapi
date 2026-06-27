Feature: Restful API Object Management
  As an API consumer
  I want to manage objects via CRUD endpoints
  So that data remains consistent

  Background:
    Given the API client is authenticated and ready

  @Smoke
  Scenario: Query all objects
    When I query all objects
    Then the response status code should be 200

  @Regression
  Scenario: Create and verify complete E2E lifecycle of an object
    When I create a new object with the following details:
      | name           | Apple MacBook Ultra |
      | year           |                2026 |
      | price          |             2849.99 |
      | CPU model      | Intel Core i9       |
      | Hard disk size |                5 TB |
    Then the response status code should be 200
    And the response JSON should validate the creation details
    When I retrieve the created object by its ID
    Then the response status code should be 200
    And the fetched object details must match the created object
    When I delete the created object
    Then the response status code should be 200
    And the deletion message should confirm the object was deleted
  # ---
  # --- Negative Scenarios ---  

  @Negative @Validation
  Scenario: Error Handling - Fetching a non-existent object
    When I attempt to retrieve an object with ID "non_existent_id_12345"
    Then the response status code should be 404
    And the error message should contain "not found"
  # ---
  # --- Edge-case Scenarios ---

  @EdgeCase
  Scenario: Idempotency Validation - Deleting an already deleted object
    Given an object has been created and subsequently deleted
    When I attempt to delete the same object again
    Then the response status code should be 404
    And the error response should contain an error message
    # And the error message should contain "doesn't exist"
    # And the error message should contain "was not found"
