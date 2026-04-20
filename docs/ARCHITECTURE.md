# Architecture Overview

Bakcend service, provide a API REST for a Frontend application.

## 1. Project Structure

[Project Root]/
├── main/              # Contains all server-side code and APIs
│   ├── src/              # Main source code for backend services
│   │   ├── api/          # API endpoints and controllers
│   │   ├── client/       # Business logic and service implementations
│   │   ├── models/       # Database models/schemas
│   │   └── utils/        # Backend utility functions
│   ├── config/           # Backend configuration files
│   ├── tests/            # Backend unit and integration tests
│   └── Dockerfile        # Dockerfile for backend deployment
├── test/             # Contains all client-side code for user interfaces
│   ├── java/              # Test files
├── docs/                 # Project documentation (e.g., API docs, setup guides)
├── scripts/              # Automation scripts (e.g., deployment, data seeding)
├── .github/              # GitHub Actions or other CI/CD configurations
├── .gitignore            # Specifies intentionally untracked files to ignore
├── README.md             # Project overview and quick start guide



## 2. High-Level System Diagram

[User] <--> [API/REST] <--> [Backend Service] <--> [Database]                           

## 3. Core Components

- Languaje: Java
- Framework: Quarkus framework:
- Database: Mongo:

#### Libraries e Integrations

- Azure OpenAI
- Mongo database
- LangChain4j

