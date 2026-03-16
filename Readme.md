# Backend Spring — Java (Spring Boot + Kafka + MySQL)

A RESTful backend service built with **Java** and **Spring Boot**, using **Kafka** for event streaming, **MySQL** as the database, and **Gradle** as the build tool.

---

## 🗂 Project Structure

```
backend_spring/
├── src/
│   └── main/
│       ├── java/com/example/backend_spring/
│       │   ├── controller/
│       │   │   └── UserController.java          # User HTTP handlers
│       │   ├── dto/
│       │   │   ├── OrderCreatedEvent.java        # Kafka event DTO
│       │   │   └── OrderItemEvent.java           # Order item DTO
│       │   ├── kafka/
│       │   │   └── OrderCreatedConsumer.java     # Kafka consumer
│       │   ├── model/
│       │   │   ├── Order.java                    # Order entity
│       │   │   ├── Product.java                  # Product entity
│       │   │   └── User.java                     # User entity
│       │   ├── repository/
│       │   │   ├── OrderRepository.java          # Order DB operations
│       │   │   ├── ProductRepository.java        # Product DB operations
│       │   │   └── UserRepository.java           # User DB operations
│       │   ├── service/
│       │   │   ├── OrderProcessingService.java   # Order business logic
│       │   │   └── UserService.java              # User business logic
│       │   └── BackendSpringApplication.java     # Application entry point
│       └── resources/
│           ├── static/                           # Static assets
│           ├── templates/                        # Template files
│           └── application.properties            # App configuration
├── test/                                         # Unit & integration tests
├── .gitignore
├── build.gradle                                  # Gradle build config
├── gradlew                                       # Gradle wrapper (Linux/Mac)
├── gradlew.bat                                   # Gradle wrapper (Windows)
└── settings.gradle
```

---

## ⚙️ Prerequisites

Make sure the following are installed:

- [Java JDK](https://adoptium.net/) `>= 17`
- [Apache Kafka](https://kafka.apache.org/downloads) `>= 3.x`
- [MySQL](https://dev.mysql.com/downloads/) `>= 8.0`
- [Git](https://git-scm.com/)

> No need to install Gradle separately — the project uses the **Gradle wrapper** (`./gradlew`).

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/backend_spring.git
cd backend_spring
```

### 2. Configure `application.properties`

Edit `src/main/resources/application.properties`:

```properties
# MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/yourdb
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Kafka
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=order-group
spring.kafka.consumer.auto-offset-reset=earliest
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer
spring.kafka.consumer.properties.spring.json.trusted.packages=*

# Server
server.port=9000
```

### 3. Add MySQL Dependency in `build.gradle`

Make sure your `build.gradle` includes the MySQL connector:

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    runtimeOnly 'com.mysql:mysql-connector-j'
    // ... other dependencies
}
```

### 4. Start Kafka (if running locally)

```bash
# Start Zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties

# Start Kafka broker (in a new terminal)
bin/kafka-server-start.sh config/server.properties
```

---

## 🔨 Build

```bash
# Linux / Mac
./gradlew build

# Windows
gradlew.bat build
```

This compiles the project and runs tests. The output JAR is placed in `build/libs/`.

---

## ▶️ Run

### Option 1 — Run with Gradle wrapper (recommended for development)

```bash
# Linux / Mac
./gradlew bootRun

# Windows
gradlew.bat bootRun
```

### Option 2 — Run the built JAR

```bash
# Step 1: Build
./gradlew build

# Step 2: Run the JAR
java -jar build/libs/backend_spring-0.0.1-SNAPSHOT.jar
```

The server starts on:
```
http://localhost:9000
```

---

## 🌐 API Endpoints

Base URL: `http://localhost:9000`

### Users

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/users/createUser` | Create a new user |
| GET | `/users/getUsers` | Get all users |
| GET | `/users/{id}/orders` | Get orders for a user by ID |

---

## 📬 API Request Examples

### POST `/users/createUser`

**✅ Valid Request**
```json
POST /users/createUser
Content-Type: application/json

{
  "name": "Ayush Kumar",
  "email": "ayush@example.com"
}
```
**Response — 200 OK**
```json
{
  "id": 1,
  "name": "Ayush Kumar",
  "email": "ayush@example.com"
}
```

**❌ Invalid — Missing `name`**
```json
{
  "email": "ayush@example.com"
}
```
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "name: must not be blank"
}
```

**❌ Invalid — Bad email format**
```json
{
  "name": "Ayush Kumar",
  "email": "not-an-email"
}
```
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "email: must be a well-formed email address"
}
```

**❌ Invalid — Empty body**
```json
{}
```
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Required request body is missing"
}
```

---

### GET `/users/getUsers`

**✅ Valid Request**
```
GET /users/getUsers
```
**Response — 200 OK**
```json
[
  { "id": 1, "name": "Ayush Kumar",  "email": "ayush@example.com" },
  { "id": 2, "name": "Jane Doe",     "email": "jane@example.com"  }
]
```

**❌ No users in DB**
```json
[]
```

---

### GET `/users/{id}/orders`

**✅ Valid Request**
```
GET /users/1/orders
```
**Response — 200 OK**
```json
"Orders for user 1: [...]"
```

**❌ Invalid — User ID does not exist**
```
GET /users/9999/orders
```
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "User not found"
}
```

**❌ Invalid — ID is not a number**
```
GET /users/abc/orders
```
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Failed to convert value of type String to Long"
}
```

---

## 📨 Kafka Integration

This service **consumes** events from the Kafka topic `order-created` via `OrderCreatedConsumer`.

```
Go Service (Producer)              Kafka                   Spring Service (Consumer)
─────────────────────             ───────                  ────────────────────────
POST /api/orders/createorder  ──► order-created  ──────►  OrderCreatedConsumer
                                    topic                  .consume(OrderCreatedEvent)
                                                                │
                                                     OrderProcessingService
                                                         .processOrder(event)
```

**Event payload expected on topic `order-created`:**
```json
{
  "orderId": 55,
  "userId": 1,
  "items": [
    { "productId": 101, "quantity": 2 },
    { "productId": 205, "quantity": 1 }
  ]
}
```

---

## 🔒 CORS

This Spring service runs on port `9000` and is called by the Go service on port `8080`. Ensure CORS is configured if calling from a browser.

---

## 🧰 Tech Stack

| Technology | Purpose |
|------------|---------|
| [Java 17+](https://adoptium.net/) | Core language |
| [Spring Boot](https://spring.io/projects/spring-boot) | Web framework |
| [Spring Data JPA](https://spring.io/projects/spring-data-jpa) | ORM / Repository layer |
| [Apache Kafka](https://kafka.apache.org/) | Event consumption |
| [MySQL](https://www.mysql.com/) | Relational database |
| [Gradle](https://gradle.org/) | Build tool |
| [Jakarta Validation](https://beanvalidation.org/) | Request body validation (`@Valid`) |

---

## 📦 Useful Gradle Commands

```bash
# Run the app
./gradlew bootRun

# Build JAR
./gradlew build

# Run tests only
./gradlew test

# Clean build output
./gradlew clean

# Clean and rebuild
./gradlew clean build
```

---

## 📝 Notes

- `@Valid` is used on `createUser` — Spring automatically validates the request body and returns `400 Bad Request` for invalid input.
- The Kafka consumer (`OrderCreatedConsumer`) listens on topic `order-created` with group ID `order-group`.
- On startup, ensure **Kafka is running** before the app starts, or the consumer coordinator will timeout (as seen in the terminal logs).
- This service is part of a **microservice pair** — the Go service (`localhost:8080`) produces Kafka events that this Spring service consumes.