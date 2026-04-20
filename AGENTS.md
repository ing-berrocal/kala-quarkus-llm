# AGENTS.md

## Project Overview

This project is a Quarkus-based Java application for responding to messages about Kala products, providing product and contact information, and managing customer interactions.

### Main Features
- **Posts**: Register and manage authorized social media posts for automated agent responses.
- **Comments**: View and respond to customer comments, either automatically (agent) or manually (human).
- **Chat**: Admin/support chat interface with the Kala agent.

See [docs/mision.md](docs/mision.md) for detailed business flows, data structures, and examples.

## Build and Run Instructions

- **Dev mode:**
  - `./mvnw quarkus:dev`
  - Dev UI: [http://localhost:8080/q/dev/](http://localhost:8080/q/dev/)
- **Package (JVM):**
  - `./mvnw package`
  - Run: `java -jar target/quarkus-app/quarkus-run.jar`
- **Package (über-jar):**
  - `./mvnw package -Dquarkus.package.jar.type=uber-jar`
  - Run: `java -jar target/*-runner.jar`
- **Native executable:**
  - `./mvnw package -Dnative`
  - Or: `./mvnw package -Dnative -Dquarkus.native.container-build=true`
  - Run: `./target/llm-kala-1.0.0-SNAPSHOT-runner`

## Testing

- `./mvnw test`

## Docker Deployment

- JVM mode: `src/main/docker/Dockerfile.jvm`
- Legacy jar: `src/main/docker/Dockerfile.legacy-jar`
- Native: `src/main/docker/Dockerfile.native`
- Native micro: `src/main/docker/Dockerfile.native-micro`

See Dockerfiles for build and run instructions for each mode.

## Key Files and Directories

- `pom.xml`: Maven configuration, dependencies, plugins, and build profiles
- `src/main/java/com/kala/`: Main Java source code (models, resources, services)
- `src/main/resources/application.properties`: Main configuration (API keys, endpoints, MongoDB, LangChain4j, etc.)
- `src/main/resources/prompt/`: Prompt templates
- `README.md`: Quickstart and Quarkus links
- `docs/mision.md`: Project mission, business flows, and data model

## Conventions and Notes

- Uses Java 21 and Quarkus 3.34.5
- Integrates with MongoDB (see application.properties for connection details)
- Integrates with LangChain4j (OpenAI/Azure/OLLAMA endpoints configurable)
- Facebook API integration for post/comment management (see application.properties)
- Follows standard Quarkus and Maven project structure
- All credentials (API keys, passwords, tokens) must be stored in a `.env` file and never committed to source code or application.properties. Reference them via environment variables.
- For more details, see [README.md](README.md) and [docs/mision.md](docs/mision.md)
---

This file helps AI coding agents quickly understand how to build, test, and deploy the project, and where to find key documentation and code.
