# Wiqaytna Pro - Database Design Documentation

## Overview

The Wiqaytna Pro database is designed using PostgreSQL and follows **Third Normal Form (3NF)** principles to ensure data integrity, reduce redundancy, and optimize query performance.

## Database Schema

### Entity-Relationship Summary

- **Users** (base table for all user types)
- **Doctors** (extends Users with medical professional information)
- **Patients** (extends Users with patient medical information)
- **Appointments** (links Doctors and Patients)
- **TimeSlots** (defines Doctor availability)

## Table Descriptions

### 1. users

**Purpose**: Base table storing all user accounts (doctors, patients, assistants).

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | SERIAL | PRIMARY KEY | Auto-increment user ID |
| email | VARCHAR(255) | UNIQUE, NOT NULL | User email (used for login) |
| password | VARCHAR(255) | NOT NULL | BCrypt hashed password |
| role | user_role | NOT NULL | ENUM: DOCTOR, PATIENT, ASSISTANT |
| first_name | VARCHAR(100) | NOT NULL | User's first name |
| last_name | VARCHAR(100) | NOT NULL | User's last name |
| phone | VARCHAR(20) | NOT NULL | Contact phone number |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Account creation date |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Last update timestamp |
| is_active | BOOLEAN | DEFAULT TRUE | Account status |

**Indexes**:
- `idx_users_email` on email (for login queries)
- `idx_users_role` on role (for role-based queries)

**Rationale**: Centralizes authentication and basic user information. Using a role column allows flexible user type management without complex table inheritance.

---

### 2. doctors

**Purpose**: Stores doctor-specific professional information.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | SERIAL | PRIMARY KEY | Doctor ID |
| user_id | INTEGER | UNIQUE, NOT NULL, FK to users(id) | Reference to user account |
| specialization | VARCHAR(100) | NOT NULL | Medical specialization |
| license_number | VARCHAR(50) | UNIQUE, NOT NULL | Professional license number |
| cabinet_address | TEXT | NOT NULL | Practice location |
| bio | TEXT | | Professional biography |
| consultation_fee | DECIMAL(10,2) | | Consultation price in MAD |
| years_of_experience | INTEGER | CHECK >= 0 | Years of practice |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Record creation date |

**Indexes**:
- `idx_doctors_user_id` on user_id
- `idx_doctors_specialization` on specialization (for search queries)

**Relationships**:
- One-to-One with users (via user_id)
- One-to-Many with time_slots
- One-to-Many with appointments

**Rationale**: Separates doctor-specific data from base user table, following Single Responsibility Principle. Composition over inheritance approach.

---

### 3. patients

**Purpose**: Stores patient medical information.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | SERIAL | PRIMARY KEY | Patient ID |
| user_id | INTEGER | UNIQUE, NOT NULL, FK to users(id) | Reference to user account |
| date_of_birth | DATE | NOT NULL | Patient's birthdate |
| medical_history | TEXT | | Past medical conditions |
| blood_group | VARCHAR(5) | | Blood type |
| allergies | TEXT | | Known allergies |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Record creation date |

**Indexes**:
- `idx_patients_user_id` on user_id

**Relationships**:
- One-to-One with users (via user_id)
- One-to-Many with appointments

**Rationale**: Maintains patient health information separately for medical record keeping and privacy concerns.

---

### 4. time_slots

**Purpose**: Defines weekly availability schedule for doctors.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | SERIAL | PRIMARY KEY | Time slot ID |
| doctor_id | INTEGER | NOT NULL, FK to doctors(id) | Reference to doctor |
| day_of_week | INTEGER | NOT NULL, CHECK 0-6 | 0=Sunday, 6=Saturday |
| start_time | TIME | NOT NULL | Slot start time |
| end_time | TIME | NOT NULL | Slot end time |
| is_available | BOOLEAN | DEFAULT TRUE | Availability status |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Record creation date |

**Constraints**:
- UNIQUE (doctor_id, day_of_week, start_time) - prevents duplicate slots
- CHECK (end_time > start_time) - validates time range

**Indexes**:
- `idx_time_slots_doctor_id` on doctor_id
- `idx_time_slots_day` on day_of_week

**Relationships**:
- Many-to-One with doctors

**Rationale**: Allows flexible scheduling with recurring weekly patterns. Days stored as integers for easy calculation.

---

### 5. appointments

**Purpose**: Core table linking doctors and patients for scheduled consultations.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | SERIAL | PRIMARY KEY | Appointment ID |
| doctor_id | INTEGER | NOT NULL, FK to doctors(id) | Reference to doctor |
| patient_id | INTEGER | NOT NULL, FK to patients(id) | Reference to patient |
| appointment_date | DATE | NOT NULL | Appointment date |
| appointment_time | TIME | NOT NULL | Appointment time |
| status | appointment_status | DEFAULT 'SCHEDULED' | ENUM status |
| notes | TEXT | | Additional notes |
| reminder_sent | BOOLEAN | DEFAULT FALSE | SMS reminder status |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Booking creation date |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Last modification |
| created_by | INTEGER | FK to users(id) | User who created booking |

**Constraints**:
- UNIQUE (doctor_id, appointment_date, appointment_time) - prevents double booking
- CHECK (appointment_date >= CURRENT_DATE) - future dates only

**Indexes**:
- `idx_appointments_doctor_id` on doctor_id
- `idx_appointments_patient_id` on patient_id
- `idx_appointments_date` on appointment_date
- `idx_appointments_status` on status

**Relationships**:
- Many-to-One with doctors
- Many-to-One with patients

**Rationale**: Implements the core business logic of the platform. Status tracking enables workflow management.

---

## Normalization

### Third Normal Form (3NF) Compliance

**First Normal Form (1NF)**:
- All tables have atomic values (no repeating groups)
- Each table has a primary key
- No duplicate rows

**Second Normal Form (2NF)**:
- Meets 1NF requirements
- All non-key attributes fully depend on the primary key
- No partial dependencies

**Third Normal Form (3NF)**:
- Meets 2NF requirements
- No transitive dependencies
- All non-key attributes depend only on the primary key

**Example**: Doctor name is stored in `users` table, not duplicated in `doctors` or `appointments`. Appointments reference doctor_id, which links to user information through the doctors table.

---

## Enum Types

### user_role
- DOCTOR
- PATIENT
- ASSISTANT

### appointment_status
- SCHEDULED - Initial booking state
- CONFIRMED - Doctor confirmed appointment
- CANCELLED - Appointment cancelled
- COMPLETED - Consultation finished
- NO_SHOW - Patient did not attend

---

## Triggers and Functions

### update_updated_at_column()

Automatically updates the `updated_at` timestamp when records are modified.

Applied to:
- users table
- appointments table

---

## Relationships Diagram

```
users (1) -------- (1) doctors
  |                      |
  |                      |
  |                      | (1)
  |                      |
  |                      | (*)
  |                 time_slots
  |
  |
  | (1)
  |
  | (1)
patients
  |
  |
  | (*)
  |
appointments (*) ------- (*) doctors
```

---

## Performance Optimizations

1. **Indexes**: Strategic indexes on foreign keys and frequently queried columns
2. **Constraints**: Database-level validation reduces application logic
3. **Enum Types**: More efficient than VARCHAR for fixed options
4. **Timestamp Triggers**: Automatic tracking without application overhead

---

## Security Considerations

1. **Password Storage**: Passwords hashed with BCrypt (never plain text)
2. **Email Validation**: Regex constraint at database level
3. **Foreign Key Constraints**: ON DELETE CASCADE/RESTRICT prevents orphaned records
4. **Role-Based Access**: User roles enable permission checking

---

## Future Enhancements

Potential additions for production deployment:

1. **Audit Log Table**: Track all data modifications
2. **Doctor Reviews**: Patient feedback system
3. **Medical Records**: Document storage references
4. **Payment Tracking**: Transaction history
5. **Notification Preferences**: User communication settings

---

## Conclusion

The database design balances simplicity with scalability, following SOLID principles at the data layer. The normalized structure ensures data integrity while maintaining query performance through strategic indexing.
