# RestAPI Test Suite

A clean, modern E2E and API testing suite built with **Spring Boot**, **Cucumber BDD**, and **RestAssured** for verifying the RESTful Objects API.

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

## Reporting [![Cucumber Reports](https://img.shields.io/badge/reports-Cucumber-green.svg)](#cucumber-test-reports)

This project includes Cucumber BDD test execution reports to help visualize scenario results, step‑level execution, and failure details.
After running the test suite (mvn test), Cucumber generates structured reports under:
target/cucumber-reports/
The reporting bundle typically includes:
- HTML Report — human‑readable summary of all features, scenarios, and steps
- JSON Report — machine‑readable output for CI/CD integrations

These reports provide:
- Clear pass/fail breakdown of all scenarios
- Step‑level execution logs and error traces
- Tags, hooks, and scenario metadata
- Easy debugging for failed steps

If you open the HTML report locally, you’ll see a structured dashboard showing:
- Feature‑wise grouping
- Scenario execution timeline
- Failed step stack traces
- Embedded logs or screenshots (if configured)

This makes it easier to track test coverage, debug failures, and monitor regression stability across builds.

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
