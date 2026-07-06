# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project state

Freshly scaffolded Spring Boot application (a single `init` commit). Only the generated entrypoint (`MesApplication`) and context-load test exist so far — there is no domain code, no controllers, entities, or repositories yet. Expect to be building the initial structure rather than fitting into an existing one.

## Stack

- **Spring Boot 4.1.0** on **Java 17** (Maven parent-managed dependency versions; no version pins in `pom.xml`).
- **Spring MVC** + **Thymeleaf** for server-rendered web (not a REST-only API by default).
- **Spring Data JPA** with the **MySQL** connector as the runtime driver.
- **Lombok** — enabled via annotation processor in `maven-compiler-plugin`. Annotations like `@Data`, `@Getter`, `@Builder` will generate code at compile time; there are no hand-written getters/setters to look for.

## Commands

Use the Maven wrapper (`./mvnw` on Unix, `mvnw.cmd` on Windows — this repo's platform).

```powershell
mvnw.cmd spring-boot:run          # run the app (default port 8080)
mvnw.cmd test                     # run all tests
mvnw.cmd test -Dtest=ClassName    # run a single test class
mvnw.cmd test -Dtest=ClassName#methodName   # run a single test method
mvnw.cmd clean package            # build the jar (target/mes-0.0.1-SNAPSHOT.jar)
mvnw.cmd verify                   # full build + tests
```

## Database

MySQL is a required runtime dependency but **is not yet configured** — `application.properties` only sets the app name. Before the app (or any `@SpringBootTest` that needs a real context) will start against a database, add `spring.datasource.*` and JPA settings there. The context-load test currently passes only because no datasource beans require a live connection yet; adding JPA repositories will change that.
