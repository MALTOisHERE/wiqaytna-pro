# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Wiqaytna Pro** is a medical appointment management SaaS platform for independent medical practices in Morocco. It digitizes appointment booking with a Spring Boot backend and React frontend.

## Technology Stack

### Backend
- **Java 17** with **Spring Boot 3.2.1**
- **PostgreSQL** database
- **Spring Security** + **JWT** authentication
- **Maven** build tool
- **Lombok** for boilerplate reduction
- **Spring Data JPA** for database access

### Frontend
- **React 18** with **Vite** build tool
- **React Router** for navigation
- **Axios** for HTTP client
- **Context API** for state management (AuthContext)

## Development Commands

### Backend (from `backend/` directory)

```bash
# Build project
mvn clean install

# Run application (default port 8080)
mvn spring-boot:run

# Run tests
mvn test

# Run specific test class
mvn test -Dtest=ClassName

# Run specific test method
mvn test -Dtest=ClassName#methodName

# Generate code coverage report (JaCoCo)
mvn test jacoco:report
# Report available at: target/site/jacoco/index.html

# Skip tests during build
mvn clean install -DskipTests

# Package application (creates JAR)
mvn package
```

### Frontend (from `frontend/` directory)

```bash
# Install dependencies
npm install

# Run development server (default port 3000)
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview
```

### Database Setup

```bash
# Create database
psql -U postgres
CREATE DATABASE wiqaytna_db;
\q

# Run schema script
psql -U postgres -d wiqaytna_db -f database/schema.sql

# Load sample data (optional)
psql -U postgres -d wiqaytna_db -f database/sample-data.sql
```

## Architecture

### Backend Architecture (Layered MVC)

The backend follows a strict layered architecture:

```
Controller Layer (REST API endpoints)
    ↓
Service Layer (Business logic)
    ↓
Repository Layer (Data access via JPA)
    ↓
Entity Models (Domain objects)
```

**Package Structure:**
- `com.wiqaytna.controller` - REST controllers handle HTTP requests/responses
- `com.wiqaytna.service` - Business logic services (AppointmentService, DoctorService, PatientService, UserService, SMSService)
- `com.wiqaytna.repository` - JPA repositories for database access
- `com.wiqaytna.model` - Entity classes (User, Doctor, Patient, Appointment, TimeSlot)
- `com.wiqaytna.dto` - Data Transfer Objects for API contracts
- `com.wiqaytna.security` - JWT authentication (JwtUtil, JwtAuthenticationFilter)
- `com.wiqaytna.config` - SecurityConfig for Spring Security configuration
- `com.wiqaytna.exception` - Custom exceptions and global exception handlers

**Key Design Patterns:**
- Dependency Injection throughout (constructor injection preferred)
- Repository pattern for data access
- DTO pattern to decouple API contracts from domain models
- Service layer encapsulates all business logic

### Frontend Architecture (Component-Based)

```
Pages (Route-level components)
    ↓
Reusable Components
    ↓
Services (API integration)
    ↓
Context (Global state - AuthContext)
```

**Directory Structure:**
- `src/pages/` - Route components (LoginPage, RegisterPage, DoctorDashboard, PatientDashboard)
- `src/components/` - Reusable UI components (PrivateRoute for auth guards)
- `src/services/` - API service modules (authService, doctorService, appointmentService, api.js for axios config)
- `src/context/` - React Context providers (AuthContext for authentication state)
- `src/styles/` - CSS stylesheets
- `src/constants/` - Shared constants and configuration

**Routing:**
- `/login` - Login page
- `/register` - Registration page
- `/doctor/dashboard` - Doctor dashboard (protected, requires DOCTOR role)
- `/patient/dashboard` - Patient dashboard (protected, requires PATIENT role)

## Authentication Flow

1. User logs in via `/api/auth/login` endpoint
2. Backend validates credentials and returns JWT token
3. Frontend stores token in AuthContext
4. Token sent in `Authorization: Bearer <token>` header for protected endpoints
5. JwtAuthenticationFilter validates token on each request
6. SecurityConfig defines which endpoints require authentication

**Roles:**
- `DOCTOR` - Medical professionals managing appointments
- `PATIENT` - Patients booking appointments
- `ASSISTANT` - Administrative staff (planned, not fully implemented)

## Database Schema

The database follows **Third Normal Form (3NF)** with these key tables:

- `users` - Base user authentication (email, password, role, name, phone)
- `doctors` - Doctor-specific data (specialization, license_number, cabinet_address, consultation_fee)
- `patients` - Patient medical data (date_of_birth, medical_history, blood_group, allergies)
- `appointments` - Booking records linking doctors and patients
- `time_slots` - Doctor availability schedule

**Key Relationships:**
- `users` (1:1) `doctors` (1:*) `appointments` (*:1) `patients` (1:1) `users`
- `doctors` (1:*) `time_slots`

**Important Constraints:**
- UNIQUE constraint on (doctor_id, appointment_date, appointment_time) prevents double-booking
- CHECK constraint ensures appointment_date >= CURRENT_DATE
- Foreign keys with CASCADE/RESTRICT for data integrity

See `docs/database-design.md` for complete schema documentation.

## Environment Configuration

Both frontend and backend use `.env` files for configuration.

### Backend `.env` (required before running)

```bash
cd backend
cp .env.example .env
# Edit .env with your values
```

**Critical variables:**
- `DB_PASSWORD` - PostgreSQL password
- `JWT_SECRET` - **Must be at least 256 bits**. Generate: `openssl rand -base64 64`
- `DB_PORT` - Default is 5433 (adjust if your PostgreSQL uses different port)
- `JPA_DDL_AUTO` - Use `update` for dev, `validate` for production

### Frontend `.env` (optional, has defaults)

```bash
cd frontend
cp .env.example .env
# Defaults work for local development
```

**Key variable:**
- `VITE_API_BASE_URL` - Backend API URL (default: http://localhost:8080/api)

## API Conventions

**Base URL:** `http://localhost:8080/api`

**Authentication Endpoints:**
- `POST /api/auth/login` - Login (returns JWT token)
- `POST /api/auth/register` - User registration

**Protected Endpoints (require Bearer token):**
- `GET /api/doctors` - List all doctors
- `GET /api/doctors/search?keyword={term}` - Search doctors by name/specialization
- `POST /api/appointments` - Create appointment
- `GET /api/appointments/doctor/{id}/upcoming` - Doctor's upcoming appointments
- `GET /api/appointments/patient/{id}/upcoming` - Patient's upcoming appointments
- `PUT /api/appointments/{id}/status?status={STATUS}` - Update appointment status
- `DELETE /api/appointments/{id}` - Cancel appointment

**Appointment Status Values:**
- `SCHEDULED` - Initial booking state
- `CONFIRMED` - Doctor confirmed
- `CANCELLED` - Cancelled by patient or doctor
- `COMPLETED` - Consultation finished
- `NO_SHOW` - Patient didn't attend

## Testing

### Backend Tests
- Unit tests using JUnit 5 and Mockito
- Test files located in `backend/src/test/java/`
- Code coverage reports generated by JaCoCo plugin

### Sample Test Data
The `database/sample-data.sql` provides test credentials:
- Doctor: `dr.amrani@wiqaytna.ma` / `password123`
- Patient: `ahmed.idrissi@gmail.com` / `password123`

## Code Style and Conventions

### Backend
- Follow SOLID principles (Single Responsibility, Dependency Inversion, etc.)
- Use constructor injection for dependencies
- Services handle business logic, controllers only handle HTTP concerns
- DTOs for API contracts, entities for database models
- Use Lombok annotations (@Data, @Builder, @NoArgsConstructor, @AllArgsConstructor)
- Method names: camelCase
- Class names: PascalCase

### Frontend
- Functional components with hooks (no class components)
- PropTypes or TypeScript for prop validation (currently using PropTypes implicitly)
- Keep components focused (single responsibility)
- Extract API calls to service modules
- Use Context API for global state (avoid prop drilling)
- File names: PascalCase for components, camelCase for utilities

## Important Implementation Notes

### SMS Reminder System
- SMS notifications are **mocked** by default (`SMS_MOCK_MODE=true`)
- Real SMS integration requires configuring external API (Twilio/Octopush)
- Scheduler runs hourly to send reminders 24 hours before appointments
- Implementation in `SMSService.java`

### Security Considerations
- Passwords hashed with BCrypt (never store plain text)
- JWT tokens expire after 24 hours (configurable via `JWT_EXPIRATION`)
- CORS configured in SecurityConfig (update `CORS_ALLOWED_ORIGINS` for production)
- SQL injection prevented by JPA parameterized queries
- Input validation using Spring Validation annotations

### Database Migrations
- JPA can auto-update schema (`JPA_DDL_AUTO=update`) in development
- For production, use `validate` mode and manage schema changes via SQL scripts
- All schema changes should be tracked in `database/` directory

## Common Development Workflows

### Adding a New Entity
1. Create entity class in `model/` package
2. Create repository interface in `repository/` package
3. Create service class in `service/` package
4. Create controller in `controller/` package
5. Create DTO classes in `dto/` package
6. Update database schema in `database/schema.sql`
7. Write tests for service and controller layers

### Adding a New Frontend Feature
1. Create service method in appropriate service file (`src/services/`)
2. Create/update page or component in `src/pages/` or `src/components/`
3. Add routing in `App.jsx` if needed
4. Update AuthContext if feature requires new permissions

### Debugging Backend
- Check application logs for stack traces
- SQL queries are logged when `JPA_SHOW_SQL=true`
- Security-related issues: check logs with `LOG_LEVEL_SECURITY=DEBUG`
- Use breakpoints in IDE or add `System.out.println()` statements

### Debugging Frontend
- Check browser console for JavaScript errors
- Check Network tab for API request/response details
- Verify token is being sent in Authorization header
- Use React DevTools to inspect component state
