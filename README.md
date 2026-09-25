# Task Manager API

REST API for managing tasks, developed with Java and Spring Boot.

## Overview

Task Manager API is a backend application that provides CRUD operations for managing tasks.

The project was developed as a portfolio project to apply and demonstrate backend development practices with Java, Spring Boot, REST APIs, data persistence, validation, testing and API documentation.

## Technologies

* Java 26
* Spring Boot 4.1
* Spring Web
* Spring Data JPA
* Hibernate
* PostgreSQL
* Maven
* JUnit 5
* Mockito
* OpenAPI / Swagger
* Git

## Features

* Create a task
* Retrieve all tasks
* Retrieve a task by ID
* Update a task
* Delete a task
* Request validation
* Standardized validation error responses
* PostgreSQL persistence
* Automated tests
* OpenAPI / Swagger API documentation

## Architecture

The application follows a layered architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
PostgreSQL
```

DTOs are used to separate API request/response models from the persistence entity.

```text
HTTP Request
     ↓
TaskController
     ↓
TaskService
     ↓
TaskRepository
     ↓
PostgreSQL
```

Validation is handled at the API boundary using Jakarta Validation, while `GlobalExceptionHandler` provides a standardized JSON response for validation errors.

## API Endpoints

Base URL:

```text
http://localhost:8080/api/tasks
```

| Method | Endpoint          | Description        | Response                                       |
| ------ | ----------------- | ------------------ | ---------------------------------------------- |
| GET    | `/api/tasks`      | Retrieve all tasks | `200 OK`                                       |
| GET    | `/api/tasks/{id}` | Retrieve a task    | `200 OK` / `404 Not Found`                     |
| POST   | `/api/tasks`      | Create a task      | `201 Created` / `400 Bad Request`              |
| PUT    | `/api/tasks/{id}` | Update a task      | `200 OK` / `400 Bad Request` / `404 Not Found` |
| DELETE | `/api/tasks/{id}` | Delete a task      | `204 No Content` / `404 Not Found`             |

## Example

### Create a task

Request:

```http
POST /api/tasks
Content-Type: application/json
```

```json
{
  "title": "Complete OpenAPI documentation",
  "completed": false
}
```

Response:

```json
{
  "id": 1,
  "title": "Complete OpenAPI documentation",
  "completed": false
}
```

### Validation

The task title must contain between 3 and 100 characters.

For example:

```json
{
  "title": "",
  "completed": false
}
```

returns:

```json
{
  "status": 400,
  "error": "Validation failed",
  "message": "Le titre est obligatoire"
}
```

## API Documentation

The API is documented using OpenAPI and Swagger UI.

Once the application is running, Swagger UI is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

The generated OpenAPI specification is available at:

```text
http://localhost:8080/v3/api-docs
```

Swagger UI can be used to explore and test all API endpoints directly from the browser.

## Configuration

The application uses PostgreSQL for data persistence.

Database credentials are provided through environment variables:

```text
DB_USERNAME
DB_PASSWORD
```

Example PowerShell configuration:

```powershell
[System.Environment]::SetEnvironmentVariable("DB_USERNAME", "postgres", "User")
[System.Environment]::SetEnvironmentVariable("DB_PASSWORD", "postgres", "User")
```

The application expects a PostgreSQL database named:

```text
task_manager
```

The database URL is configured in `application.properties`.

Credentials are intentionally kept outside the source code.

## Running the application

### Prerequisites

* JDK 26
* PostgreSQL
* Git

### Start the application

Clone the repository, configure the database environment variables, then run:

```powershell
.\mvnw.cmd spring-boot:run
```

The API will be available at:

```text
http://localhost:8080
```

### Run the tests

```powershell
.\mvnw.cmd clean test
```

Current test status:

```text
21 tests
21 passed
0 failures
0 errors
```

## Project Structure

```text
src/
├── main/
│   ├── java/
│   │   └── com.example.taskmanagerapi/
│   │       ├── config/
│   │       │   └── OpenApiConfig.java
│   │       ├── controller/
│   │       │   └── TaskController.java
│   │       ├── dto/
│   │       │   ├── TaskRequest.java
│   │       │   └── TaskResponse.java
│   │       ├── entity/
│   │       │   └── Task.java
│   │       ├── exception/
│   │       │   ├── ErrorResponse.java
│   │       │   └── GlobalExceptionHandler.java
│   │       ├── repository/
│   │       │   └── TaskRepository.java
│   │       ├── service/
│   │       │   └── TaskService.java
│   │       └── TaskManagerApiApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/
        └── com.example.taskmanagerapi/
            ├── controller/
            │   └── TaskControllerTest.java
            ├── service/
            │   └── TaskServiceTest.java
            └── TaskManagerApiApplicationTests.java
```

## Testing

The project currently contains unit and integration tests covering the main application behaviour.

```text
Tests run: 21
Failures: 0
Errors: 0
Skipped: 0
```

The tests cover controller behaviour, service logic and application context loading.

## Future improvements

Possible future developments include:

* Docker / Docker Compose
* CI pipeline
* Database migration with Flyway
* Authentication and authorization
* Pagination and filtering
* Additional API integration tests
* Production-oriented configuration profiles