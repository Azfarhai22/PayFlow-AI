# PayFlow AI

AI-powered Payment and Transaction Management System built using Java 21 and Spring Boot.

## Current Progress

- Identity Service setup
- User registration API
- Password encryption using BCrypt
- User login API
- JWT access token generation

## Tech Stack

- Java 21
- Spring Boot
- Spring Security
- PostgreSQL
- JWT
- Maven

## API Endpoints

### Identity Service

Base URL: `http://localhost:8080`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/auth/register` | Register a new user |
| POST | `/api/v1/auth/login` | Authenticate user and generate JWT |

### Registration Request

```json
{
  "fullName": "Rahul Sharma",
  "email": "rahul@example.com",
  "password": "Rahul@123"
}
```

### Login Request

```json
{
  "email": "rahul@example.com",
  "password": "Rahul@123"
}
```

**Login Response:** Returns user details and a JWT access token.

