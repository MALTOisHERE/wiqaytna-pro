# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Wiqaytna Pro** is a medical appointment management SaaS platform for independent medical practices in Morocco. It's a full-stack application with a Spring Boot backend and React frontend, using JWT authentication and PostgreSQL.

## Development Commands

### Backend (Spring Boot + Maven)

```bash
# From backend/ directory
mvn clean install          # Build project and install dependencies
mvn spring-boot:run        # Run backend server (http://localhost:8080)
mvn test                   # Run all tests
mvn test -Dtest=ClassName  # Run single test class
mvn clean                  # Clean build artifacts
```

### Frontend (React + Vite)

```bash
# From frontend/ directory
npm install                # Install dependencies
npm run dev               # Start dev server (http://localhost:3000 or http://localhost:5173)
npm run build             # Build for production
npm run preview           # Preview production build
```

### Database (PostgreSQL)

```bash
# Create database
psql -U postgres
CREATE DATABASE wiqaytna_db;
\q

# Run schema
psql -U postgres -d wiqaytna_db -f database/schema.sql

# Load sample data
psql -U postgres -d wiqaytna_db -f database/sample-data.sql
```

## Architecture

### Backend Architecture (Layered MVC)

The backend follows a strict layered architecture with clear separation of concerns:

```
Controller Layer (REST API)
    ↓
Service Layer (Business Logic)
    ↓
Repository Layer (JPA/Data Access)
    ↓
Model Layer (Entity Classes)
    ↓
PostgreSQL Database
```

**Key architectural patterns:**

1. **Entity-User Relationship Pattern**: `User` is the base authentication entity. `Doctor` and `Patient` extend it via `user_id` foreign keys. Always fetch related user data when working with Doctor/Patient entities.

2. **DTO Pattern**: API requests/responses use DTOs (in `dto/` package), never expose entities directly. Service layer handles entity↔DTO mapping.

3. **JWT Authentication Flow**:
   - `SecurityConfig.java` configures Spring Security with stateless sessions
   - `JwtAuthenticationFilter.java` intercepts requests, validates tokens
   - `JwtUtil.java` handles token generation/validation
   - Public endpoints: `/api/auth/**`, `/api/doctors/search`, `/api/doctors/{id}`

4. **Appointment Booking Logic** (in `AppointmentService.java`):
   - Check slot availability before booking
   - Resolve patient ID (frontend may send user_id instead of patient_id)
   - Send SMS confirmation via `SMSService` (mocked implementation)
   - Scheduled task sends reminders 24 hours before appointment

5. **Database Constraints**: Schema uses PostgreSQL ENUM types (`user_role`, `appointment_status`) and CHECK constraints. These are enforced at DB level, so validation errors may come from PostgreSQL.

### Frontend Architecture (Component-Based)

```
Pages (Route Components in pages/)
    ↓
Reusable Components (components/)
    ↓
Services (API Integration in services/)
    ↓
Context (AuthContext for global auth state)
```

**Key patterns:**

1. **Authentication Context**: `AuthContext.jsx` provides global auth state. Use `useAuth()` hook to access user, login, logout, register functions.

2. **Private Routing**: `PrivateRoute.jsx` component wraps protected routes, checks authentication and role-based access (e.g., `requiredRole="DOCTOR"`).

3. **API Service Pattern**:
   - `api.js` configures Axios with base URL and interceptors
   - Token automatically added to requests via interceptor
   - 401 responses trigger automatic logout and redirect
   - Service files (`authService.js`, `doctorService.js`, `appointmentService.js`) wrap API calls

4. **Role-Based Dashboards**: Separate dashboard components for doctors (`DoctorDashboard.jsx`) and patients (`PatientDashboard.jsx`). Routing ensures users only access their role's dashboard.

### Database Schema

**Core relationships:**
- `users` (1:1) → `doctors` OR `patients` (via `user_id`)
- `doctors` (1:*) → `appointments` (*:1) ← `patients`
- `doctors` (1:*) → `time_slots` (availability schedule)

**Important constraints:**
- Email validation via CHECK constraint with regex
- Future appointment dates enforced via CHECK constraint
- Unique constraint on doctor timeslots prevents double-booking
- ENUM types for `user_role` and `appointment_status`

## Key Implementation Details

### Authentication

- **Password Storage**: BCrypt hashing via Spring Security's `PasswordEncoder`
- **Token Format**: JWT tokens include user_id, email, and role claims
- **Token Location**: Frontend stores token in localStorage
- **CORS**: Configured in `SecurityConfig.java` to allow localhost:3000 and localhost:5173

### Appointment Management

- **Status Flow**: SCHEDULED → CONFIRMED → COMPLETED (or CANCELLED/NO_SHOW)
- **Reminder System**: Scheduled task runs hourly (cron: `0 0 * * * *`), sends SMS 24 hours before appointment
- **SMS Service**: Currently mocked (console output). For real implementation, integrate Twilio/Octopush API
- **Cancellation Rules**: Appointments can only be cancelled if not already completed/cancelled (enforced in `Appointment.java` entity)

### ID Resolution Pattern

**Critical**: The appointment booking flow handles both user_id and patient_id:
- Frontend may send `user_id` as `patientId` in appointment requests
- `AppointmentService.createAppointment()` attempts to resolve via `PatientService.getPatientByUserId()`
- Falls back to treating it as direct patient_id if resolution fails
- This dual-resolution prevents frontend/backend ID confusion

## Common Development Patterns

### Adding New Endpoints

1. Define DTO in `dto/` package
2. Add method to appropriate Repository interface
3. Implement business logic in Service class
4. Create REST endpoint in Controller
5. Add frontend service method in `services/`
6. Update frontend components

### Testing Credentials (from sample-data.sql)

**Doctor:**
- Email: `dr.amrani@wiqaytna.ma`
- Password: `password123`

**Patient:**
- Email: `ahmed.idrissi@gmail.com`
- Password: `password123`

### Error Handling

- Backend: `GlobalExceptionHandler.java` catches exceptions and returns consistent error responses
- Frontend: API interceptor in `api.js` handles 401 errors with automatic logout
- Service methods throw `RuntimeException` with descriptive messages (consider creating custom exception classes for production)

## Configuration Files

### Backend Configuration

**File**: `backend/src/main/resources/application.properties`

Key settings:
- Database URL, username, password
- JWT secret key
- Scheduler cron expressions
- JPA/Hibernate settings

### Frontend Configuration

**File**: `frontend/src/services/api.js`

Change `API_BASE_URL` if backend runs on different host/port (default: `http://localhost:8080/api`).

## Important Conventions

1. **Commit Messages**: Follow conventional commits format (`feat:`, `fix:`, `docs:`, `style:`, `refactor:`, `test:`, `chore:`)

2. **Java Naming**:
   - Entities use JPA annotations (`@Entity`, `@Table`, `@Column`)
   - Repositories extend `JpaRepository<Entity, Long>`
   - Services use `@Service`, `@Transactional`
   - Controllers use `@RestController`, `@RequestMapping`

3. **React Naming**:
   - Components use PascalCase with `.jsx` extension
   - Services use camelCase with `.js` extension
   - CSS files mirror component names

4. **SOLID Principles**: Code follows SOLID design:
   - Single Responsibility: Each class has one purpose
   - Dependency Injection: Constructor/field injection via `@Autowired`
   - Interface Segregation: Repositories use focused interfaces

## Development Workflow

1. Start PostgreSQL database
2. Run backend: `cd backend && mvn spring-boot:run`
3. Run frontend: `cd frontend && npm run dev`
4. Access application at http://localhost:3000 or http://localhost:5173

## Known Issues and Gotchas

1. **Port Conflicts**: Frontend may use port 3000 or 5173 depending on Vite version. CORS configured for both.

2. **Patient ID Resolution**: When creating appointments, be aware of the user_id vs patient_id distinction. The backend handles both.

3. **Time Zone Handling**: Currently using LocalDate/LocalTime without time zone handling. All times assumed to be Moroccan local time.

4. **SMS Service**: Currently mocked. Real implementation requires API key configuration and integration with SMS provider.

5. **Scheduled Tasks**: Reminder scheduler may not run in development due to Spring Boot DevTools. Use production profile for full testing.

6. **Database Constraints**: PostgreSQL ENUM types require explicit type casting in raw SQL queries. JPA handles this automatically.
