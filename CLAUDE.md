# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
# Build entire project
mvn clean install

# Build without tests
mvn clean install -DskipTests

# Run the application (port 8080)
mvn spring-boot:run -pl rest-mvc

# Run all tests
mvn clean test

# Run tests for a specific module
mvn test -pl domain
mvn test -pl rest-mvc

# Run a specific test class
mvn test -Dtest=BeerControllerTest

# Run a specific test method
mvn test -Dtest=BeerControllerTest#testGetBeerById
```

## Architecture

This is a **multi-module Maven project** with three modules forming a strict dependency chain:

```
rest-mvc → service → domain
```

- **domain** — JPA entities (`Beer`, `Customer`) and Spring Data repositories. No business logic.
- **service** — Business logic interfaces and implementations (`BeerService`, `CustomerService`). Depends on domain.
- **rest-mvc** — Spring Boot application entry point, REST controllers, DTOs, MapStruct mappers, view services, and exception handling. Depends on service.

### REST Layer Internals (rest-mvc)

The controller layer uses a **View Service** abstraction (`BeerViewService`, `CustomerViewService`) that sits between controllers and the business service layer. View services handle DTO↔entity conversion using MapStruct mappers before delegating to the `service` module.

Flow: `Controller → ViewService → (Mapper + Service) → Repository`

API base path: `/api/v1/` — resources are `beers` and `customers`.

### Key Technologies

- Spring Boot 4.0.x (parent: `spring-boot-starter-parent:4.0.2`)
- Java 25
- Spring Data JPA + H2 (in-memory)
- MapStruct 1.6.3 for entity↔DTO mapping (annotation processor)
- Lombok for boilerplate reduction (annotation processor)
- MockMvc + Mockito for controller tests; `@DataJpaTest` for repository tests

### Custom Test Infrastructure

- `@DataJpaTestImpl` (in `domain/src/test`) — composite annotation that combines `@DataJpaTest`, `@EnableJpaRepositories`, `@EntityScan`, and `@Import(TestConfig.class)`. Use this instead of bare `@DataJpaTest` for repository tests in this project.

### MapStruct + Lombok Configuration

Both Lombok and MapStruct are configured as annotation processor paths in the parent `pom.xml`. The `lombok-mapstruct-binding` dependency ensures correct processing order. When adding new mappers, follow the existing pattern in `rest-mvc/src/main/java/.../mapper/`.

### Exception Handling

`ExceptionManager` (`@RestControllerAdvice`) centralizes HTTP error responses. Throw `NotFoundException` for missing resources — it maps to HTTP 404.

### Database Initialization

`DbDataBootstrapper` (implements `CommandLineRunner`) seeds Beer and Customer sample data on startup when the database is empty.
