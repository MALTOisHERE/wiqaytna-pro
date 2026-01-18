-- Wiqaytna Pro Database Schema
-- PostgreSQL Database for Medical Appointment Management
-- Normalized database design following 3NF principles

-- Drop tables if they exist (for clean re-creation)
DROP TABLE IF EXISTS appointments CASCADE;
DROP TABLE IF EXISTS time_slots CASCADE;
DROP TABLE IF EXISTS patients CASCADE;
DROP TABLE IF EXISTS doctors CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TYPE IF EXISTS user_role CASCADE;
DROP TYPE IF EXISTS appointment_status CASCADE;

-- Create ENUM types for better data integrity
CREATE TYPE user_role AS ENUM ('DOCTOR', 'PATIENT', 'ASSISTANT');
CREATE TYPE appointment_status AS ENUM ('SCHEDULED', 'CONFIRMED', 'CANCELLED', 'COMPLETED', 'NO_SHOW');

-- Users table (base table for all user types)
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role user_role NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    CONSTRAINT email_format CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}$')
);

-- Doctors table (extends users with doctor-specific information)
CREATE TABLE doctors (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    specialization VARCHAR(100) NOT NULL,
    license_number VARCHAR(50) NOT NULL UNIQUE,
    cabinet_address TEXT NOT NULL,
    bio TEXT,
    consultation_fee DECIMAL(10, 2),
    years_of_experience INTEGER CHECK (years_of_experience >= 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Patients table (extends users with patient-specific information)
CREATE TABLE patients (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    date_of_birth DATE,  -- Nullable to allow profile completion after registration
    medical_history TEXT,
    blood_group VARCHAR(5),
    allergies TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT valid_age CHECK (date_of_birth IS NULL OR date_of_birth <= CURRENT_DATE - INTERVAL '1 year')
);

-- Time slots table (doctor availability)
CREATE TABLE time_slots (
    id SERIAL PRIMARY KEY,
    doctor_id INTEGER NOT NULL REFERENCES doctors(id) ON DELETE CASCADE,
    day_of_week INTEGER NOT NULL CHECK (day_of_week BETWEEN 0 AND 6), -- 0=Sunday, 6=Saturday
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT valid_time_range CHECK (end_time > start_time),
    CONSTRAINT unique_doctor_timeslot UNIQUE (doctor_id, day_of_week, start_time)
);

-- Appointments table (the core booking entity)
CREATE TABLE appointments (
    id SERIAL PRIMARY KEY,
    doctor_id INTEGER NOT NULL REFERENCES doctors(id) ON DELETE RESTRICT,
    patient_id INTEGER NOT NULL REFERENCES patients(id) ON DELETE RESTRICT,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    status appointment_status DEFAULT 'SCHEDULED',
    notes TEXT,
    reminder_sent BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER REFERENCES users(id),
    CONSTRAINT future_appointment CHECK (appointment_date >= CURRENT_DATE),
    CONSTRAINT unique_doctor_appointment UNIQUE (doctor_id, appointment_date, appointment_time)
);

-- Indexes for performance optimization
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_doctors_user_id ON doctors(user_id);
CREATE INDEX idx_doctors_specialization ON doctors(specialization);
CREATE INDEX idx_patients_user_id ON patients(user_id);
CREATE INDEX idx_time_slots_doctor_id ON time_slots(doctor_id);
CREATE INDEX idx_time_slots_day ON time_slots(day_of_week);
CREATE INDEX idx_appointments_doctor_id ON appointments(doctor_id);
CREATE INDEX idx_appointments_patient_id ON appointments(patient_id);
CREATE INDEX idx_appointments_date ON appointments(appointment_date);
CREATE INDEX idx_appointments_status ON appointments(status);

-- Function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to automatically update updated_at for users
CREATE TRIGGER update_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Trigger to automatically update updated_at for appointments
CREATE TRIGGER update_appointments_updated_at
    BEFORE UPDATE ON appointments
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Comments for documentation
COMMENT ON TABLE users IS 'Base table storing all user accounts (doctors, patients, assistants)';
COMMENT ON TABLE doctors IS 'Doctor-specific information linked to users table';
COMMENT ON TABLE patients IS 'Patient-specific information linked to users table';
COMMENT ON TABLE time_slots IS 'Weekly availability schedule for doctors';
COMMENT ON TABLE appointments IS 'Appointment bookings between doctors and patients';

COMMENT ON COLUMN users.role IS 'User type: DOCTOR, PATIENT, or ASSISTANT';
COMMENT ON COLUMN appointments.status IS 'Current status: SCHEDULED, CONFIRMED, CANCELLED, COMPLETED, NO_SHOW';
COMMENT ON COLUMN appointments.reminder_sent IS 'Flag to track if SMS reminder was sent';
COMMENT ON COLUMN time_slots.day_of_week IS 'Day of week: 0=Sunday, 1=Monday, ..., 6=Saturday';
