# Sprint_7
## Automated API tests for "Yandex Samokat"
Tests cover scenarios of using API endpoints for creating a courier, authenticating a courier, creating orders, and fetching order lists.
Tests are implemented with Java, JUnit, RestAssured, and generate Allure reports.

## Tech Stack

| Technology      | Version   |
|-----------------|-----------|
| Java            | 11        |
| Maven           | 3.9.10    |
| JUnit           | 4.13.1    |
| RestAssured     | 5.2.0     |
| Gson            | 2.8.9     |
| AspectJ Weaver  | 1.9.7     |
| Maven Surefire  | 3.2.5     |
| Allure          | 2.16.0    |

## Start project

### Installation
Install dependencies:  
`mvn clean install`

### Run all tests
To run all tests:  
`mvn clean test`

## Allure report

### Open an already generated report
To open an already generated report, run:  
`allure open target/allure-report`

### Generate new report
After running all tests, a new Allure report can be generated and opened:  
`mvn allure:serve`