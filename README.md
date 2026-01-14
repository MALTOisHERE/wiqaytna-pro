# Wiqaytna Pro - Medical Appointment Management Platform

## Team Information

**Project**: Wiqaytna Pro - Gestion des Rendez-vous Médicaux
**Institution**: [Your University/Institution]
**Academic Year**: 2025-2026

**Team Members**:
- **Name**: Fatima Zahra AIT OUAARAB
- **Email**: [your-email@example.com]
- **Role**: Full-Stack Developer

---

## Table of Contents

1. [Project Description](#project-description)
2. [Features](#features)
3. [Technology Stack](#technology-stack)
4. [Architecture Overview](#architecture-overview)
5. [Project Structure](#project-structure)
6. [Prerequisites](#prerequisites)
7. [Installation & Setup](#installation--setup)
8. [Running the Application](#running-the-application)
9. [API Documentation](#api-documentation)
10. [Database Schema](#database-schema)
11. [UML Diagrams](#uml-diagrams)
12. [Git Workflow](#git-workflow)
13. [Testing](#testing)
14. [Best Practices Applied](#best-practices-applied)
15. [Future Enhancements](#future-enhancements)

---

## Project Description

**Wiqaytna Pro** is a Software-as-a-Service (SaaS) platform designed for independent medical practices in Morocco. The platform digitalizes the appointment booking process, replacing traditional paper-based systems with a modern, efficient solution.

### Problem Solved

Small medical practices (dentists, general practitioners, specialists) still rely on paper notebooks for appointment management, leading to:
- Overcrowded waiting rooms
- Missed appointments
- Poor time management
- No patient communication

### Solution

Wiqaytna Pro provides:
- **For Doctors**: Calendar management, patient tracking, automated reminders
- **For Patients**: Online booking, real-time availability, SMS reminders
- **For Assistants**: Administrative tools, appointment management

---

## Features

### Doctor Features
- View appointments in calendar format
- Manage availability time slots
- Access patient medical history
- Update appointment status (Confirm/Complete/Cancel)
- View upcoming appointments

### Patient Features
- Search doctors by name or specialization
- View doctor profiles (specialization, experience, consultation fee)
- Book appointments online
- View upcoming appointments
- Receive SMS reminders (mocked)
- Cancel appointments

### Assistant Features
- Manage appointments on behalf of doctors
- Handle patient records
- Administrative support

### System Features
- JWT-based authentication
- Automated SMS reminders (24 hours before appointment)
- Real-time availability checking
- Responsive design (mobile-friendly)

---

## Technology Stack

### Backend
- **Java 17**
- **Spring Boot 3.2.1**
- **Spring Data JPA** (Database access)
- **Spring Security** (Authentication)
- **PostgreSQL** (Database)
- **Maven** (Build tool)
- **JWT (JSON Web Tokens)** (Authentication)
- **Lombok** (Boilerplate reduction)

### Frontend
- **React 18** (UI framework)
- **React Router** (Navigation)
- **Axios** (HTTP client)
- **Vite** (Build tool)
- **CSS3** (Styling)

### Tools & Methodologies
- **PlantUML** (UML diagrams)
- **Git** (Version control)
- **SOLID Principles**
- **MVC Architecture**
- **RESTful API Design**

---

## Architecture Overview

### Backend Architecture (MVC + Layered)

```
┌─────────────────────────────────────────┐
│          REST Controllers               │  ← API Layer
├─────────────────────────────────────────┤
│          Service Layer                  │  ← Business Logic
├─────────────────────────────────────────┤
│          Repository Layer               │  ← Data Access (JPA)
├─────────────────────────────────────────┤
│          Entity Models                  │  ← Domain Objects
├─────────────────────────────────────────┤
│          PostgreSQL Database            │  ← Persistence
└─────────────────────────────────────────┘
```

### Frontend Architecture (Component-Based)

```
┌─────────────────────────────────────────┐
│          Pages (Routes)                 │  ← Route Components
├─────────────────────────────────────────┤
│          Reusable Components            │  ← UI Components
├─────────────────────────────────────────┤
│          Services (API Calls)           │  ← API Integration
├─────────────────────────────────────────┤
│          Context (State Management)     │  ← Global State
└─────────────────────────────────────────┘
```

---

## Project Structure

```
wiqaytna_pro/
│
├── backend/                        # Spring Boot Backend
│   ├── src/main/java/com/wiqaytna/
│   │   ├── config/                 # Configuration classes
│   │   ├── controller/             # REST Controllers
│   │   ├── service/                # Business Logic
│   │   ├── repository/             # JPA Repositories
│   │   ├── model/                  # Entity Classes
│   │   ├── dto/                    # Data Transfer Objects
│   │   ├── security/               # JWT & Security
│   │   └── exception/              # Exception Handling
│   ├── src/main/resources/
│   │   ├── application.properties  # App Configuration
│   │   └── db/                     # SQL Scripts
│   └── pom.xml                     # Maven Dependencies
│
├── frontend/                       # React Frontend
│   ├── src/
│   │   ├── components/             # Reusable Components
│   │   ├── pages/                  # Page Components
│   │   ├── services/               # API Services
│   │   ├── context/                # React Context
│   │   ├── styles/                 # CSS Stylesheets
│   │   ├── App.jsx                 # Main App Component
│   │   └── main.jsx                # Entry Point
│   ├── index.html                  # HTML Template
│   ├── package.json                # NPM Dependencies
│   └── vite.config.js              # Vite Configuration
│
├── database/                       # Database Scripts
│   ├── schema.sql                  # Database Schema
│   └── sample-data.sql             # Sample Data
│
├── uml/                            # UML Diagrams (PlantUML)
│   ├── use-case.puml               # Use Case Diagram
│   ├── class-diagram.puml          # Class Diagram
│   ├── sequence-booking.puml       # Booking Sequence
│   └── sequence-reminder.puml      # Reminder Sequence
│
├── docs/                           # Documentation
│   └── database-design.md          # Database Design Doc
│
├── .gitignore                      # Git Ignore Rules
└── README.md                       # This File
```

---

## Prerequisites

Before you begin, ensure you have the following installed:

1. **Java Development Kit (JDK) 17 or higher**
   - Download: https://www.oracle.com/java/technologies/downloads/
   - Verify: `java -version`

2. **Maven 3.6+**
   - Download: https://maven.apache.org/download.cgi
   - Verify: `mvn -version`

3. **PostgreSQL 13+**
   - Download: https://www.postgresql.org/download/
   - Verify: `psql --version`

4. **Node.js 18+ and npm**
   - Download: https://nodejs.org/
   - Verify: `node -version` and `npm -version`

5. **Git**
   - Download: https://git-scm.com/downloads
   - Verify: `git --version`

6. **PlantUML** (Optional, for viewing/editing UML diagrams)
   - Download: https://plantuml.com/download

---

## Installation & Setup

### Step 1: Clone the Repository

```bash
git clone <repository-url>
cd wiqaytna_pro
```

### Step 2: Environment Variables Setup

The application uses `.env` files to manage configuration. This keeps sensitive information secure and makes deployment easier.

#### Backend Environment Variables

1. **Copy the example file**:
```bash
cd backend
cp .env.example .env
```

2. **Edit `backend/.env`** with your configuration:
```properties
# Database Configuration
DB_HOST=localhost
DB_PORT=5433
DB_NAME=wiqaytna_db
DB_USERNAME=postgres
DB_PASSWORD=your_secure_password_here

# JWT Configuration (IMPORTANT: Change in production!)
JWT_SECRET=your_strong_random_secret_key_minimum_256_bits
JWT_EXPIRATION=86400000

# Generate strong JWT secret:
# openssl rand -base64 64
```

**Important Variables**:
- `DB_PASSWORD`: Your PostgreSQL password
- `JWT_SECRET`: **CRITICAL** - Must be at least 256 bits. Generate using `openssl rand -base64 64`
- `CORS_ALLOWED_ORIGINS`: Update for production domain
- `JPA_DDL_AUTO`: Use `update` for development, `validate` for production

#### Frontend Environment Variables

1. **Copy the example file**:
```bash
cd frontend
cp .env.example .env
```

2. **Edit `frontend/.env`** (optional, defaults work for local development):
```properties
VITE_API_BASE_URL=http://localhost:8080/api
VITE_APP_NAME=Wiqaytna Pro
```

**Important**: Frontend variables must start with `VITE_` prefix to be accessible.

### Step 3: Database Setup

1. **Start PostgreSQL** service

2. **Create Database**:
```bash
psql -U postgres
CREATE DATABASE wiqaytna_db;
\q
```

3. **Run Schema Script**:
```bash
psql -U postgres -d wiqaytna_db -f database/schema.sql
```

4. **Load Sample Data** (Optional):
```bash
psql -U postgres -d wiqaytna_db -f database/sample-data.sql
```

**Note**: Database credentials are now configured via `.env` file (see Step 2), not in `application.properties`.

### Step 4: Backend Setup

Navigate to backend directory:
```bash
cd backend
```

Install dependencies and build:
```bash
mvn clean install
```

### Step 5: Frontend Setup

Navigate to frontend directory:
```bash
cd ../frontend
```

Install dependencies:
```bash
npm install
```

---

## Running the Application

### Start Backend (Terminal 1)

```bash
cd backend
mvn spring-boot:run
```

Backend will start on: **http://localhost:8080**

### Start Frontend (Terminal 2)

```bash
cd frontend
npm run dev
```

Frontend will start on: **http://localhost:3000**

### Access the Application

Open your browser and navigate to: **http://localhost:3000**

---

## API Documentation

### Base URL
```
http://localhost:8080/api
```

### Authentication Endpoints

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "email": "patient@example.com",
  "password": "password123",
  "role": "PATIENT",
  "firstName": "Ahmed",
  "lastName": "Idrissi",
  "phone": "+212661234567"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "patient@example.com",
  "password": "password123"
}

Response:
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "user": { ...user details... }
}
```

### Doctor Endpoints

#### Get All Doctors
```http
GET /api/doctors
Authorization: Bearer <token>
```

#### Search Doctors
```http
GET /api/doctors/search?keyword=dentiste
```

#### Get Doctor by ID
```http
GET /api/doctors/{id}
```

### Appointment Endpoints

#### Create Appointment
```http
POST /api/appointments
Authorization: Bearer <token>
Content-Type: application/json

{
  "doctorId": 1,
  "patientId": 1,
  "appointmentDate": "2026-01-20",
  "appointmentTime": "10:00:00",
  "notes": "Consultation de routine"
}
```

#### Get Doctor's Appointments
```http
GET /api/appointments/doctor/{doctorId}/upcoming
Authorization: Bearer <token>
```

#### Get Patient's Appointments
```http
GET /api/appointments/patient/{patientId}/upcoming
Authorization: Bearer <token>
```

#### Update Appointment Status
```http
PUT /api/appointments/{id}/status?status=CONFIRMED
Authorization: Bearer <token>
```

#### Cancel Appointment
```http
DELETE /api/appointments/{id}
Authorization: Bearer <token>
```

---

## Database Schema

See full documentation: [docs/database-design.md](docs/database-design.md)

### Key Tables:
- **users**: Base authentication table
- **doctors**: Doctor profiles and information
- **patients**: Patient medical records
- **appointments**: Booking records
- **time_slots**: Doctor availability

### ER Diagram Summary:
```
users (1:1) doctors (1:*) appointments (*:1) patients (1:1) users
                    |
                    └─── (1:*) time_slots
```

---

## UML Diagrams

All UML diagrams are created with PlantUML and located in the `uml/` directory:

1. **Use Case Diagram** (`use-case.puml`): Shows actors and their interactions
2. **Class Diagram** (`class-diagram.puml`): Shows all entities and relationships
3. **Sequence Diagram - Booking** (`sequence-booking.puml`): Patient booking flow
4. **Sequence Diagram - Reminder** (`sequence-reminder.puml`): SMS reminder process

To view diagrams, use PlantUML or online viewers like: http://www.plantuml.com/plantuml/

---

## Git Workflow

### Recommended Commit Sequence

This project follows a structured commit history. Here's the recommended sequence:

```bash
# 1. Initial Setup
git add .gitignore
git commit -m "chore: Add .gitignore file"

# 2. Database
git add database/
git commit -m "feat: Add database schema and sample data

- Created normalized PostgreSQL schema (3NF)
- Added sample data with Moroccan context
- Implemented proper constraints and indexes"

# 3. UML Diagrams
git add uml/use-case.puml
git commit -m "docs: Add use case diagram"

git add uml/class-diagram.puml
git commit -m "docs: Add class diagram with all entities"

git add uml/sequence-*.puml
git commit -m "docs: Add sequence diagrams for booking and reminders"

# 4. Backend - Initial Setup
git add backend/pom.xml backend/src/main/resources/application.properties
git commit -m "feat: Initialize Spring Boot backend with Maven

- Added all required dependencies
- Configured PostgreSQL connection
- Set up JWT authentication"

# 5. Backend - Models
git add backend/src/main/java/com/wiqaytna/model/
git commit -m "feat: Add entity models with JPA annotations

- Created User, Doctor, Patient, Appointment, TimeSlot entities
- Applied proper validation and relationships
- Followed SOLID principles"

# 6. Backend - Repositories
git add backend/src/main/java/com/wiqaytna/repository/
git commit -m "feat: Add repository layer with custom queries

- Implemented JPA repositories for all entities
- Added custom query methods for business logic"

# 7. Backend - Security
git add backend/src/main/java/com/wiqaytna/security/
git add backend/src/main/java/com/wiqaytna/config/
git commit -m "feat: Implement JWT authentication and security

- Created JWT utility class
- Added authentication filter
- Configured Spring Security"

# 8. Backend - DTOs
git add backend/src/main/java/com/wiqaytna/dto/
git commit -m "feat: Add Data Transfer Objects

- Created DTOs for API requests/responses
- Separated internal entities from API contract"

# 9. Backend - Services
git add backend/src/main/java/com/wiqaytna/service/
git commit -m "feat: Implement service layer with business logic

- Created UserService, DoctorService, PatientService
- Implemented AppointmentService with booking logic
- Added SMSService with mock implementation"

# 10. Backend - Controllers
git add backend/src/main/java/com/wiqaytna/controller/
git commit -m "feat: Add REST controllers

- Created AuthController for authentication
- Implemented CRUD controllers for all entities
- Applied proper HTTP status codes and error handling"

# 11. Backend - Exception Handling
git add backend/src/main/java/com/wiqaytna/exception/
git commit -m "feat: Add global exception handling

- Created GlobalExceptionHandler
- Implemented consistent error responses"

# 12. Frontend - Initial Setup
git add frontend/package.json frontend/vite.config.js frontend/index.html
git commit -m "feat: Initialize React frontend with Vite

- Set up Vite build tool
- Added required dependencies
- Configured proxy for backend API"

# 13. Frontend - API Services
git add frontend/src/services/
git commit -m "feat: Implement API services

- Created Axios configuration with JWT interceptors
- Implemented auth, appointment, and doctor services"

# 14. Frontend - Context
git add frontend/src/context/
git commit -m "feat: Add authentication context

- Created AuthContext for global state
- Implemented authentication methods"

# 15. Frontend - Auth Pages
git add frontend/src/pages/LoginPage.jsx frontend/src/pages/RegisterPage.jsx
git commit -m "feat: Add authentication pages

- Created login and registration forms
- Implemented form validation"

# 16. Frontend - Dashboards
git add frontend/src/pages/DoctorDashboard.jsx
git commit -m "feat: Implement doctor dashboard

- Added appointment management
- Implemented status updates
- Created calendar view"

git add frontend/src/pages/PatientDashboard.jsx
git commit -m "feat: Implement patient dashboard

- Added doctor search functionality
- Implemented appointment booking
- Created appointment list view"

# 17. Frontend - Components
git add frontend/src/components/
git commit -m "feat: Add reusable components

- Created PrivateRoute for route protection
- Implemented component composition"

# 18. Frontend - Styling
git add frontend/src/styles/
git commit -m "style: Add responsive CSS styling

- Created modern, clean design
- Implemented responsive layout
- Added professional color scheme"

# 19. Documentation
git add docs/
git commit -m "docs: Add database design documentation

- Documented database schema
- Explained normalization approach
- Added ER diagrams and rationale"

git add README.md
git commit -m "docs: Add comprehensive README

- Added setup instructions
- Documented API endpoints
- Included architecture overview"

# 20. Final
git commit -m "chore: Final code review and cleanup"
```

### Commit Message Convention

Format: `<type>: <description>`

Types:
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation
- `style`: Code style/formatting
- `refactor`: Code refactoring
- `test`: Tests
- `chore`: Build/tooling

---

## Testing

### Backend Unit Tests

Run tests:
```bash
cd backend
mvn test
```

### Manual Testing

Test credentials (from sample data):

**Doctor**:
- Email: `dr.amrani@wiqaytna.ma`
- Password: `password123`

**Patient**:
- Email: `ahmed.idrissi@gmail.com`
- Password: `password123`

---

## Best Practices Applied

### SOLID Principles

1. **Single Responsibility**: Each class has one reason to change
   - Services handle business logic only
   - Controllers handle HTTP requests only
   - Repositories handle data access only

2. **Open/Closed**: Classes open for extension, closed for modification
   - Used interfaces for repositories
   - DTOs separate API contract from implementation

3. **Liskov Substitution**: Subtypes must be substitutable
   - Repository interfaces can be swapped

4. **Interface Segregation**: Clients shouldn't depend on unused interfaces
   - Focused service interfaces

5. **Dependency Inversion**: Depend on abstractions
   - Services depend on repository interfaces
   - Dependency injection throughout

### Clean Code Practices

- Meaningful variable and method names
- Small, focused methods
- No code duplication (DRY principle)
- Proper error handling
- Comprehensive comments for complex logic
- Consistent naming conventions (camelCase, PascalCase)

### Database Design

- Third Normal Form (3NF)
- Proper indexes for performance
- Foreign key constraints
- Check constraints for data integrity

### Security

- Password hashing with BCrypt
- JWT token authentication
- CORS configuration
- Input validation
- SQL injection prevention (JPA)
- Environment variables for sensitive data

---

## Environment Variables Reference

### Backend Variables

All backend configuration is managed via `backend/.env` file:

| Variable | Default | Description |
|----------|---------|-------------|
| `SERVER_PORT` | 8080 | Backend server port |
| `DB_HOST` | localhost | PostgreSQL host |
| `DB_PORT` | 5433 | PostgreSQL port |
| `DB_NAME` | wiqaytna_db | Database name |
| `DB_USERNAME` | postgres | Database username |
| `DB_PASSWORD` | postgres | Database password (**change in production!**) |
| `JPA_DDL_AUTO` | update | Hibernate DDL mode (use `validate` in production) |
| `JPA_SHOW_SQL` | true | Show SQL queries in logs |
| `JPA_FORMAT_SQL` | true | Format SQL queries in logs |
| `JWT_SECRET` | (weak default) | **CRITICAL**: JWT signing key (generate with `openssl rand -base64 64`) |
| `JWT_EXPIRATION` | 86400000 | Token lifetime in milliseconds (24 hours) |
| `CORS_ALLOWED_ORIGINS` | localhost:3000,5173 | Comma-separated allowed origins |
| `LOG_LEVEL_ROOT` | INFO | Root logging level |
| `LOG_LEVEL_APP` | DEBUG | Application logging level |
| `LOG_LEVEL_SECURITY` | DEBUG | Security logging level |
| `LOG_LEVEL_SQL` | DEBUG | SQL logging level |
| `SMS_ENABLED` | false | Enable SMS notifications |
| `SMS_MOCK_MODE` | true | Use mock SMS service |
| `SCHEDULER_REMINDER_ENABLED` | true | Enable appointment reminders |
| `SCHEDULER_REMINDER_CRON` | 0 0 * * * * | Cron expression for reminders |
| `MAX_FILE_SIZE` | 10MB | Maximum file upload size |
| `MAX_REQUEST_SIZE` | 10MB | Maximum request size |

### Frontend Variables

All frontend configuration is managed via `frontend/.env` file. Variables must start with `VITE_`:

| Variable | Default | Description |
|----------|---------|-------------|
| `VITE_API_BASE_URL` | http://localhost:8080/api | Backend API URL |
| `VITE_APP_NAME` | Wiqaytna Pro | Application name |
| `VITE_APP_VERSION` | 1.0.0 | Application version |
| `VITE_DEV_PORT` | 3000 | Development server port |
| `VITE_ENABLE_SMS_NOTIFICATIONS` | false | Feature flag for SMS |
| `VITE_ENABLE_EMAIL_NOTIFICATIONS` | false | Feature flag for email |
| `VITE_APPOINTMENT_BOOKING_ADVANCE_DAYS` | 30 | Days in advance for booking |
| `VITE_APPOINTMENT_CANCELLATION_HOURS` | 24 | Hours before appointment to cancel |
| `VITE_DEFAULT_TIMEZONE` | Africa/Casablanca | Default timezone |
| `VITE_DATE_FORMAT` | DD/MM/YYYY | Date display format |
| `VITE_TIME_FORMAT` | HH:mm | Time display format |

### Production Security Checklist

Before deploying to production:

- [ ] Generate strong `JWT_SECRET` (at least 256 bits)
- [ ] Update `DB_PASSWORD` to secure password
- [ ] Set `JPA_DDL_AUTO=validate` to prevent schema changes
- [ ] Update `CORS_ALLOWED_ORIGINS` to production domain only
- [ ] Set logging levels to `INFO` or `WARN`
- [ ] Update `VITE_API_BASE_URL` to production API URL
- [ ] Ensure `.env` files have restricted permissions (`chmod 600`)
- [ ] Never commit `.env` files to version control

---

## Future Enhancements

### Phase 2 Features
1. Real SMS API integration (Octopush/Twilio)
2. Email notifications
3. Doctor review system
4. Payment integration
5. Medical records upload
6. Video consultation
7. Multi-language support (Arabic/French)
8. Calendar synchronization (Google Calendar)
9. Advanced scheduling (recurring appointments)
10. Analytics dashboard for doctors

### Technical Improvements
1. Unit test coverage increase
2. Integration tests
3. Docker containerization
4. CI/CD pipeline
5. API documentation (Swagger/OpenAPI)
6. Logging and monitoring
7. Caching layer (Redis)
8. Load balancing
9. Database migration tools (Flyway/Liquibase)

---

## Troubleshooting

### Common Issues

**Backend won't start**:
- Check PostgreSQL is running
- Verify database credentials in `backend/.env`
- Ensure Java 17 is installed
- Run `mvn clean install` to rebuild
- Check if `.env` file exists in `backend/` directory

**Frontend won't start**:
- Delete `node_modules` and run `npm install` again
- Check Node.js version (requires 18+)
- Verify backend is running on port 8080
- Restart dev server after changing `.env`

**Database connection failed**:
- Verify PostgreSQL service is running
- Check database exists: `psql -U postgres -l`
- Verify credentials in `backend/.env`
- Ensure `DB_PORT` matches your PostgreSQL port (default: 5432, custom: 5433)

**CORS errors**:
- Ensure frontend runs on port 3000 or 5173
- Check `CORS_ALLOWED_ORIGINS` in `backend/.env`
- Verify `VITE_API_BASE_URL` in `frontend/.env`
- Restart backend after changing CORS settings

**Environment variables not working**:
- **Backend**: Ensure `.env` file is in `backend/` directory (not root)
- **Frontend**: Variables must start with `VITE_` prefix
- **Frontend**: Restart dev server (`npm run dev`) after changes
- Check for typos in variable names
- Verify syntax: `KEY=value` (no spaces around `=`)
- Run `mvn clean install` for backend after `.env` changes

---

## License

This project is for educational purposes as part of academic coursework.

---

## Contact

For questions or support, contact:
- **Email**: [your-email@example.com]
- **GitHub**: [your-github-username]

---

**Generated with Claude Code**
Last Updated: January 2026
