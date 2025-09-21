# Purchase Order CRUD API

A Spring Boot REST service for managing users, catalog items, and purchase orders (header + detail) backed by PostgreSQL.

## Features
- RESTful CRUD for users, items, and purchase orders
- Automatic total cost and price calculation per purchase order
- Clear DTO boundary between API payloads and entities
- Layered MVC architecture with service and repository layers
- PostgreSQL schema scripts plus Docker-based deployment
- Unit tests with JUnit 5 and Mockito using an H2 test profile

## Project Structure
```
src/main/java/com/myboost/test/
|-- controller       # REST controllers
|-- dto              # Request and response records
|-- entity           # JPA entities with auditing support
|-- exception        # Centralised exception handling
|-- mapper           # Entity <-> DTO mappers
|-- repository       # Spring Data JPA repositories
`-- service          # Service interfaces and implementations
```
Supporting assets:
- `database.sql` - PostgreSQL database and schema bootstrap script
- `docker/init.sql` - Schema initialiser used by Docker Compose
- `Dockerfile`, `docker-compose.yml` - Container build and orchestration files

## Getting Started

### Prerequisites
- Java 21
- Maven 3.9+
- PostgreSQL 16 (skip when using Docker Compose)

### Local Development
1. Execute `database.sql` on your PostgreSQL server.
2. Update credentials in `src/main/resources/application.properties`.
3. Launch the app:
   ```bash
   ./mvnw spring-boot:run
   ```

### Running Tests
```bash
./mvnw test
```
The `test` profile uses an in-memory H2 database configured in `src/test/resources/application-test.properties`.

## Docker Workflow

### Build Image
```bash
docker build -t po-crud-app .
```

### Run with Docker Compose
```bash
docker compose up --build
```
- PostgreSQL initialises with `docker/init.sql` (schema + sample data)
- Application listens on `http://localhost:8080`

Stop and remove containers:
```bash
docker compose down
```
Add `-v` to remove the persistent database volume.

## API Overview

| Resource          | Endpoint(s)                            | Notes                                       |
|-------------------|----------------------------------------|---------------------------------------------|
| Users             | `GET/POST/PUT/DELETE /api/users`        | `GET` accepts optional `id` query parameter |
| Items             | `GET/POST/PUT/DELETE /api/items`        | Same optional `id` query parameter          |
| Purchase Orders   | `GET/POST/PUT/DELETE /api/purchase-orders` | Returns header including detail lines    |

Request payloads live in `dto.request`; responses mirror `dto.response` records.

## Testing & Quality
- Service layer covered by Mockito-based unit tests in `src/test/java/com/myboost/test/service/impl`
- Spring context smoke test runs under the `test` profile
- Auditable fields managed via the `AuditableEntity` superclass

## License
Provided as-is. Align with your organisation`s licensing requirements before distributing.

