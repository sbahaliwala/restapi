# RestAPI Test Suite

A clean, modern E2E and API testing suite built with **Spring Boot**, **Cucumber BDD**, and **RestAssured** for verifying the RESTful Objects API.

---

## Tech Stack & Key Libraries

- **Framework**: Spring Boot 4.x
- **BDD Testing**: Cucumber JVM (Java & Spring integration)
- **HTTP Client**: RestAssured (REST API assertions and calls)
- **JSON Engine**: Jackson Databind (Payload serialization/deserialization)
- **Lombok**: Boilerplate reduction (Builders, Logging)
- **Test Runner**: JUnit 5 Platform Suite

---

## Configuration

Test settings are configured in `src/main/resources/application.properties`:

- `server.url`: Target base URL for the API (e.g., `https://api.restful-api.dev/objects`).
- `server.api-key`: API key header value for client requests.

---

## Running the Tests

To compile the project and run all tests, execute:

```bash
# Windows
.\mvnw clean test

# Unix/macOS
./mvnw clean test
```

### Running Specific Tests or Scenarios
You can configure cucumber filter tags or runner parameters in [CucumberTestRunner.java](file:///c:/Users/sbaha/learn/java/api/restapi/src/test/java/com/example/restapi/CucumberTestRunner.java) using configuration parameters (e.g., `@ConfigurationParameter(key = Constants.FILTER_TAGS_PROPERTY_NAME, value = "@EdgeCase")`).

To run a specific test class from the command line:
```bash
.\mvnw test -Dtest=CucumberTestRunner
```

---

## Project Structure

```text
restapi/
├── src/
│   ├── main/
│   │   ├── java/com/example/restapi/
│   │   │   └── RestapiApplication.java (Spring Boot Application entrypoint)
│   │   |   ├── models                   # POJOs for payload serialization
|    |   |
│   │   └── resources/
│   │       └── application.properties   (Configuration for target API & logs)
│   └── test/
│       ├── java/com/example/restapi/
│       │   ├── CucumberTestRunner.java (BDD Test Suite configuration)
│       │   ├── context/
│       │   │   └── TestContext.java    (Scenario state sharing context)
│       │   └── steps/
│       │       ├── CucumberSpringConfiguration.java (Spring context glue)
│       │       └── ObjectSteps.java    (Step definitions & assertions)
│       └── resources/
│           └── object_management.feature (Gherkin test scenarios)
└── pom.xml (Maven dependencies & build plugins)
└── README.md
```
