# Wiqaytna Pro - Medical Appointment Management Platform

## Team Information

**Project**: Wiqaytna Pro - Gestion des Rendez-vous Médicaux
**Institution**: INPT
**Academic Year**: 2025-2026

**Team Members**:
- **Name**: Aitouaarab Fatimazahra 
            Sajiaa Hafssa
            Chablou Amina


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
12. [Testing](#testing)
13. [Best Practices Applied](#best-practices-applied)
14. [Future Enhancements](#future-enhancements)

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

To view diagrams, there are in the report of project that is posted on moodle .

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

