# Reservation System

## Overview
This project is a high‑performance reservation system built with **Java 21**, **Spring Boot**, and **MySQL**.  
It allows users to:
- View available slots
- Reserve a slot (with concurrency handling)
- Cancel reservations

The focus is on clean code, software engineering principles, and meeting performance requirements such as response times under 200ms with millions of records.

---

## Features
- **User Authentication**: JWT‑based login system.
- **Reservation Management**:
  - List available slots with pagination.
  - Reserve a slot (concurrent safe).
  - Cancel a reservation.
- **Caching**: Caffeine cache for available slots to reduce database load.
- **Database Access**: jOOQ for type‑safe SQL queries.
- **API Documentation**: Swagger / OpenAPI integration.
- **Exception Handling**: Global exception handler with ControllerAdvice.

---

## Project Structure
```
src/main/java/ir/azki/
 ├── auth/      
 ├── config/    
 ├── controller/
 ├── data/   
 ├── error/  
 └── service/
```

---

## Key Design Decisions
- **Concurrency**: Database transactions with `@Transactional` ensure safe slot reservations.
- **Cancel Strategy**: Reservations are deleted (instead of soft delete with status) to keep slot availability consistent.
- **Caching**: Available slots cached with Caffeine (`expireAfterWrite` + `maximumSize`).
- **Docker**: `docker-compose` with `mysql` and the application container for easy deployment.

---

## Build & Run

Before building the Docker image, you need to package the application:

```bash
mvn clean package
```

Then run the docker compose:

```bash
docker compose up (or docker-compose up)
```

---

## Example API Endpoints

### Authentication
```http
POST /api/users/authenticate
Content-Type: application/json

{
  "username": "user1",
  "password": "123456"
}
```

### Get Available Slots
```http
GET /api/slots?page=0&size=10
Authorization: Bearer <jwt-token>
```

### Reserve a Slot
```http
POST /api/reservations
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "slotId": 1
}
```

### Cancel Reservation
```http
DELETE /api/reservations/{reservationId}
Authorization: Bearer <jwt-token>
```

---

## Testing & Validation
- **Concurrency Test**: Multiple users trying to reserve the same slot at the same time → only one succeeds.
- **Performance Test**: 1M+ slots in DB, average response time < 200ms.


---

## Run with Docker Compose
```yaml
version: '3.9'

services:
  mysql:
    image: mysql:latest
    container_name: reservation-mysql
    environment:
      MYSQL_ROOT_PASSWORD: rootpass
      MYSQL_DATABASE: reservation_db
      MYSQL_USER: user
      MYSQL_PASSWORD: pass
    ports:
      - "3306:3306"
    volumes:
      - mysql-data:/var/lib/mysql

  app:
    build: .
    container_name: reservation-app
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/reservation_db
      SPRING_DATASOURCE_USERNAME: user
      SPRING_DATASOURCE_PASSWORD: pass
    depends_on:
      - mysql

volumes:
  mysql-data:
```

---

## Tools & Dependencies
- Spring Boot (Web, Security)
- jOOQ
- MySQL
- Lombok
- Caffeine Cache
- Swagger / OpenAPI
- Docker & Docker Compose

---

## Authors
Developed as part of an interview project demonstrating **high performance, clean design, and concurrency handling** in a real‑world reservation system.
