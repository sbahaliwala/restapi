Feature: Sample scenarios Restful API Object Management
  As an API consumer
  I want to manage objects via CRUD endpoints
  So that data remains consistent

  Background:
    Given the API client is authenticated and ready

  @Sample
  Scenario: Ability to create an item
    Given a "Apple MacBook Pro 26" item is created
    And the CPU model is "Intel Core i9"
    And has a price of "1849.99"
    When the request to add the item is made
    Then the response status code should be 200
    And a "Apple MacBook Pro 26" is created
  # Scenario: Ability to return an item
  # Scenario: Ability to list multiple items
  # Scenario: Ability to delete an item
