# RestAPI Test Suite

A clean, modern E2E API testing suite built with **Spring**, **Cucumber BDD**, and **RestAssured** for verifying the RESTful Objects API.

---
## Requirements Implementation Summary
**1. Required Features**

- Multi-Call REST Automation: Automated end-to-end CRUD flows (e.g., creating an object with a POST request, verifying its retrieval with a GET, and performing cleanup via DELETE) in 
`object_management.feature`.
- Clean Code & Method Design: Centralized all HTTP logic into a dedicated `ApiClient` component.
- Scenario State Sharing: Using Spring-scoped bean (`TestContext` with @Scope("cucumber-glue")) to share dynamic data (such as HTTP responses, payloads, and resource IDs) between Cucumber step definitions.
- Error & Edge Case Testing: Implemented scenarios tagged with @Negative and @EdgeCase in `object_management.feature` to validate scenario for invalid IDs and idempotency checks.
- Cucumber BDD Framework: Implemented Gherkin features with a JUnit 5 runner (`CucumberTestRunner.java`) for tag‑based execution
- Robust JSON Path Assertions: Validated nested fields and dynamic keys in `ObjectSteps.java` using RestAssured JsonPath.
- Extended Test Scenarios: Added negative flows and idempotency tests beyond standard happy‑path cases.
  
**2. Nice-to-Have Features**

- Git Version Control: Clean history and local configurations tracked using Git.
- Lifecycle Setup & Cleanup Hooks: Implemented Cucumber @After lifecycle hooks in `Hooks.java` for automatic teardown and orphan cleanup.
- Conditional API Key Authentication: `ApiClient` adds x-api-key only when running under the test profile.
- Spring Boot & Lombok Integration: Used Spring DI and Lombok (@Data, @Builder, @Slf4j) to reduce boilerplate and unify logging.
---

## How to Install & Run This Spring Boot Maven Project
**Clone the Repository**: Use Git to pull the project onto your machine
```bash
git clone https://github.com/sbahaliwala/restapi.git
cd restapi
```

**Ensure Java & Maven Are Installed**
You need:
Java 17+ (or the version your project targets)
Apache Maven 3.8+

**Check versions:**
```bash
java -version
mvn -version
```

If Java is not installed, install a JDK (Temurin, Oracle, or OpenJDK).

**Build the Project**
Run a clean build to download dependencies and compile the code:
```bash
mvn clean install
```
---

## Running the Tests

To compile the project and run tests, execute:

```bash
# Windows
# run all tests
mvn test -Dcucumber.plugin="pretty" -Dcucumber.features="src/test/resources/features" -Dcucumber.plugin="pretty, html:target/cucumber-reports/cucumber.html, json:target/cucumber-reports/cucumber.json"
```
To run only specific scenarios (For example regression scenarios)
```bash
mvn test -Dcucumber.plugin="pretty" -Dcucumber.features="src/test/resources/features" -Dcucumber.filter.tags="@Regression" -Dcucumber.plugin="pretty, html:target/cucumber-reports/cucumber.html, json:target/cucumber-reports/cucumber.json"
```
Running Specific Tests or Scenarios
You can configure cucumber filter tags or runner parameters in [CucumberTestRunner.java](//restapi/src/test/java/com/example/restapi/CucumberTestRunner.java) using configuration parameters 
(e.g., `@ConfigurationParameter(key = Constants.FILTER_TAGS_PROPERTY_NAME, value = "@EdgeCase")`).
Options available are: 
- @Sample: Sample scenarios
- @Smoke: Smoke tests
- @Regression: Regression tests
- @Negative: Negative scenarios
- @EdgeCase: Edge case scenarios

---

## Reporting

This project includes Cucumber BDD test execution reports to help visualize scenario results, step‑level execution, and failure details.
After running the test suite (mvn test), Cucumber generates structured reports under: **target/cucumber-reports/**

The reporting bundle typically includes:
- HTML Report — human‑readable summary of all features, scenarios, and steps
- JSON Report — machine‑readable output for CI/CD integrations

---

## Configuration

Test settings are configured in `src/main/resources/application.properties`:

- `server.url`: Target base URL for the API (e.g., `https://api.restful-api.dev/objects`).
- `server.api-key`: API key header value for client requests.
- `spring.profiles.active`: Lets you select the environment ( eg. Development or Test or Production)
---

## Tech Stack & Key Libraries

- **Framework**: Spring Boot 4.x
- **BDD Testing**: Cucumber JVM (Java & Spring integration)
- **HTTP Client**: RestAssured (REST API assertions and calls)
- **JSON Engine**: Jackson Databind (Payload serialization/deserialization)
- **Lombok**: Boilerplate reduction (Builders, Logging)
- **Test Runner**: JUnit 5 Platform Suite

---

## Project Structure

```text
restapi/
│
├── src/
│   ├── main/
│   │   ├── java/com/example/restapi/
│   │   │   ├── RestapiApplication.java          # Spring Boot entrypoint
│   │   │   └── models/                          # POJOs for payload serialization
│   │   │
│   │   └── resources/
│   │       └── application.properties           # API + logging configuration
│   │
│   └── test/
│       ├── java/com/example/restapi/
│       │   ├── CucumberTestRunner.java          # BDD test suite runner
│       │   ├── context/
│       │   │   └── TestContext.java             # Shared scenario state
│       │   └── steps/
│       │       ├── CucumberSpringConfiguration.java   # Spring test glue
│       │       └── ObjectSteps.java             # Step definitions & assertions
│       │
│       └── resources/
│           ├── features/
│           │   └── object_management.feature    # Gherkin scenarios
│           └── CucumberSpringConfiguration.java # Spring context glue (if needed)
│
├── pom.xml                                      # Maven build + dependencies
└── README.md
```
