# Web Calendar for Repairs

A Java Spring Boot web application for managing repair jobs with a calendar and lists, server-side rendered with Thymeleaf and backed by PostgreSQL. It exposes REST endpoints for entities like jobs and products and provides HTML pages for calendar and job/product views.

Database resources repo: https://github.com/Rakibei/p3-database

## Stack

- Language: Java 21
- Frameworks/Libraries: Spring Boot 3.5.x (Web, Data JPA, Validation, DevTools), Thymeleaf
- Database: PostgreSQL (JPA/Hibernate)
- Build/Package Manager: Maven (with Maven Wrapper `mvnw`/`mvnw.cmd`)
- Env loading: `spring-dotenv` (reads `.env` at project root)
- Frontend assets: WebJars (Bootstrap 5.3, jQuery 3.7) + static resources under `src/main/resources/static`
- Optional Dev tooling: Node.js + Prettier (with `prettier-plugin-java`)

## Requirements

- Java 21 (JDK)
- Maven Wrapper (included) — no need to install Maven separately
- PostgreSQL 14+ running locally

## Environment Configuration

Configuration is done via `.env` at the project root and `src/main/resources/application.properties`.

1. Copy the example env file and edit values as needed:

```
cp env.example .env
# On Windows (PowerShell): Copy-Item env.example .env
```

2. Required environment variables (see `env.example`):

- `JDBC_DATABASE_URL` — e.g. `jdbc:postgresql://localhost:5432/appdb`
- `POSTGRES_USER` — database user
- `POSTGRES_PASSWORD` — database password

`application.properties` wires these into Spring:

```
spring.datasource.url=${JDBC_DATABASE_URL}
spring.datasource.username=${POSTGRES_USER}
spring.datasource.password=${POSTGRES_PASSWORD}
server.port=9000
spring.jpa.hibernate.ddl-auto=update
```

Notes:

- The app defaults to port `9000`.
- JPA `ddl-auto=update` will auto-create/update tables in development; consider using `validate` or migrations in production. TODO: add Flyway/Liquibase if needed.

## Setup

1. Ensure PostgreSQL is running and that the configured database exists.
2. Create `.env` (see above) with valid connection details.
3. Build the project to download dependencies:

```
./mvnw -q -DskipTests package
# On Windows: .\mvnw.cmd -q -DskipTests package
```

## Run

- Using Maven (development):

```
./mvnw spring-boot:run
# Windows: .\mvnw.cmd spring-boot:run
```

- Or use your IDE’s Spring Boot run configuration.

Then open http://localhost:9000

## Application Entry Point

- Main class: `mainProgram.MainApplication`
- Location: `src/main/java/mainProgram/MainApplication.java`

## Endpoints and Pages

- Server-rendered pages (Thymeleaf) via `PageController`:
  - `/` → redirects to `/kalender`
  - `/kalender` → calendar view (template `calendar.html`)
  - `/jobliste` → job list view (template `jobliste.html`)
  - `/jobliste/{id}` → job details view (template `jobDetails.html`)
  - `/products` → product list view (template `products.html`)
- REST APIs (examples):
  - `POST /api/products` → create product
  - `PUT  /api/products/{id}` → update product
  - `DELETE /api/products/{id}` → delete product
  - Additional controllers exist (e.g., `PartController`, `SearchController`, `JobController`); see source for full details.

## Project Structure (high level)

```
.
├─ pom.xml                           # Maven build config
├─ env.example                       # Sample environment variables
├─ src
│  ├─ main
│  │  ├─ java/mainProgram
│  │  │  ├─ MainApplication.java     # Entry point
│  │  │  ├─ controller/              # Controllers
│  │  │  ├─ repository/              # Spring Data repositories
│  │  │  ├─ initializer/             # Initializers with table data
│  │  │  ├─ services/                # Service layer
│  │  │  └─ table/                   # JPA entities
│  │  └─ resources
│  │     ├─ application.properties   # Spring configuration (port, DB, JPA, thymeleaf)
│  │     ├─ templates/               # Thymeleaf templates (calendar, jobDetails, products, ...)
│  │     └─ static/                  # Static assets (css, js, images)
│  └─ test/java/mainProgram          # Tests
├─ package.json                      # Dev tooling (Prettier)
└─ README.md
```

## Development Notes

- Live reload: `spring-boot-devtools` is included; when using your IDE or `spring-boot:run`, changes may trigger restarts.
- Thymeleaf caching is disabled in `application.properties` for easier template development.
- WebJars provide Bootstrap and jQuery without external CDNs.

## Troubleshooting

- Cannot connect to DB: verify `.env` is loaded and values match your PostgreSQL instance.
- Port already in use: change `server.port` in `application.properties` or free port 9000.
- Templates not updating: ensure template cache is disabled (it is by default in this project) and you’re running in dev mode.
