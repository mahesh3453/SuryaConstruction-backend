# SURYA CONSTRUCTION - Permit Activity Tracking Backend

This repository contains the Spring Boot backend server for the Surya Construction Permit Tracking application.

## Tech Stack
- **Framework**: Spring Boot 3.2.2 (Java 17)
- **Database ORM**: Spring Data JPA
- **Database Driver**: MySQL Connector/J (Aiven MySQL 8.4 compatible)
- **Authentication**: HttpSession cookie-based session tracking
- **Excel Report Generator**: Apache POI 5.2.5

## Production Deployment (Render + Docker)
This project is ready to be deployed on **Render** via Docker. The repository contains a multi-stage `Dockerfile` which compiles the Java project using Maven and packages it in a lightweight JRE image.

### Render Environment Settings
Ensure you add the following Environment Variables in the Render Dashboard:

1. `DB_URL` - Connection string format: `jdbc:mysql://[host]:[port]/[database]?useSSL=true&requireSSL=true`
2. `DB_USERNAME` - Database master user
3. `DB_PASSWORD` - Database master password
4. `CORS_ALLOWED_ORIGINS` - Set this to your live Vercel URL (e.g. `https://suryaconstruction.vercel.app`)

---

## Local Setup
Make sure local MySQL is running on port 3306 with a database named `suryaconstruction`.

Start server:
```bash
mvn spring-boot:run
```
The server starts on port `8080`.
Documentation: `http://localhost:8080/swagger-ui.html`.
