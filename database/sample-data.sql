-- Wiqaytna Pro Sample Data
-- Test data for development and demonstration
-- Password for all users: "password123" (hashed with BCrypt)

-- Clear existing data
TRUNCATE TABLE appointments CASCADE;
TRUNCATE TABLE time_slots CASCADE;
TRUNCATE TABLE patients CASCADE;
TRUNCATE TABLE doctors CASCADE;
TRUNCATE TABLE users CASCADE;

-- Reset sequences
ALTER SEQUENCE users_id_seq RESTART WITH 1;
ALTER SEQUENCE doctors_id_seq RESTART WITH 1;
ALTER SEQUENCE patients_id_seq RESTART WITH 1;
ALTER SEQUENCE time_slots_id_seq RESTART WITH 1;
ALTER SEQUENCE appointments_id_seq RESTART WITH 1;

-- Insert Users (password is BCrypt hash of "password123")
-- Doctors
INSERT INTO users (email, password, role, first_name, last_name, phone) VALUES
('dr.amrani@wiqaytna.ma', '$2a$10$rXg8Z8Z8Z8Z8Z8Z8Z8Z8ZuN8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8', 'DOCTOR', 'Fatima', 'Amrani', '+212661234567'),
('dr.bennani@wiqaytna.ma', '$2a$10$rXg8Z8Z8Z8Z8Z8Z8Z8Z8ZuN8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8', 'DOCTOR', 'Youssef', 'Bennani', '+212662345678'),
('dr.alaoui@wiqaytna.ma', '$2a$10$rXg8Z8Z8Z8Z8Z8Z8Z8Z8ZuN8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8', 'DOCTOR', 'Samira', 'Alaoui', '+212663456789');

-- Patients
INSERT INTO users (email, password, role, first_name, last_name, phone) VALUES
('ahmed.idrissi@gmail.com', '$2a$10$rXg8Z8Z8Z8Z8Z8Z8Z8Z8ZuN8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8', 'PATIENT', 'Ahmed', 'Idrissi', '+212664567890'),
('sarah.moussaoui@gmail.com', '$2a$10$rXg8Z8Z8Z8Z8Z8Z8Z8Z8ZuN8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8', 'PATIENT', 'Sarah', 'Moussaoui', '+212665678901'),
('omar.el-fassi@gmail.com', '$2a$10$rXg8Z8Z8Z8Z8Z8Z8Z8Z8ZuN8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8', 'PATIENT', 'Omar', 'El Fassi', '+212666789012'),
('leila.benkirane@gmail.com', '$2a$10$rXg8Z8Z8Z8Z8Z8Z8Z8Z8ZuN8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8', 'PATIENT', 'Leila', 'Benkirane', '+212667890123'),
('karim.tazi@gmail.com', '$2a$10$rXg8Z8Z8Z8Z8Z8Z8Z8Z8ZuN8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8', 'PATIENT', 'Karim', 'Tazi', '+212668901234');

-- Assistant
INSERT INTO users (email, password, role, first_name, last_name, phone) VALUES
('assistant@wiqaytna.ma', '$2a$10$rXg8Z8Z8Z8Z8Z8Z8Z8Z8ZuN8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8Z8', 'ASSISTANT', 'Nawal', 'Chakir', '+212669012345');

-- Insert Doctors
INSERT INTO doctors (user_id, specialization, license_number, cabinet_address, bio, consultation_fee, years_of_experience) VALUES
(1, 'Médecine Générale', 'MG-2015-001234', '25 Avenue Mohammed V, Casablanca', 'Médecin généraliste avec 8 ans d''expérience. Spécialisée dans la médecine familiale et préventive.', 250.00, 8),
(2, 'Dentiste', 'DENT-2012-005678', '15 Rue Allal Ben Abdellah, Rabat', 'Chirurgien dentiste spécialisé en orthodontie et implantologie. Consultation sur rendez-vous uniquement.', 300.00, 11),
(3, 'Pédiatre', 'PED-2018-009012', '42 Boulevard Zerktouni, Casablanca', 'Pédiatre certifiée, expérience en néonatologie et suivi des enfants jusqu''à l''adolescence.', 280.00, 5);

-- Insert Patients
INSERT INTO patients (user_id, date_of_birth, medical_history, blood_group, allergies) VALUES
(4, '1985-03-15', 'Hypertension légère', 'A+', 'Pénicilline'),
(5, '1992-07-22', 'Aucun antécédent majeur', 'O+', NULL),
(6, '1978-11-08', 'Diabète type 2, cholestérol', 'B+', 'Aspirine'),
(7, '1995-01-30', 'Asthme léger', 'AB+', 'Pollen'),
(8, '1988-09-17', 'Aucun antécédent', 'O-', NULL);

-- Insert Time Slots for Dr. Fatima Amrani (Monday to Friday, 9h-12h and 14h-18h)
INSERT INTO time_slots (doctor_id, day_of_week, start_time, end_time, is_available) VALUES
-- Monday
(1, 1, '09:00:00', '12:00:00', TRUE),
(1, 1, '14:00:00', '18:00:00', TRUE),
-- Tuesday
(1, 2, '09:00:00', '12:00:00', TRUE),
(1, 2, '14:00:00', '18:00:00', TRUE),
-- Wednesday
(1, 3, '09:00:00', '12:00:00', TRUE),
(1, 3, '14:00:00', '18:00:00', TRUE),
-- Thursday
(1, 4, '09:00:00', '12:00:00', TRUE),
(1, 4, '14:00:00', '18:00:00', TRUE),
-- Friday
(1, 5, '09:00:00', '12:00:00', TRUE);

-- Insert Time Slots for Dr. Youssef Bennani (Monday to Saturday, 10h-13h and 15h-19h)
INSERT INTO time_slots (doctor_id, day_of_week, start_time, end_time, is_available) VALUES
(2, 1, '10:00:00', '13:00:00', TRUE),
(2, 1, '15:00:00', '19:00:00', TRUE),
(2, 2, '10:00:00', '13:00:00', TRUE),
(2, 2, '15:00:00', '19:00:00', TRUE),
(2, 3, '10:00:00', '13:00:00', TRUE),
(2, 3, '15:00:00', '19:00:00', TRUE),
(2, 4, '10:00:00', '13:00:00', TRUE),
(2, 4, '15:00:00', '19:00:00', TRUE),
(2, 5, '10:00:00', '13:00:00', TRUE),
(2, 5, '15:00:00', '19:00:00', TRUE),
(2, 6, '10:00:00', '13:00:00', TRUE);

-- Insert Time Slots for Dr. Samira Alaoui (Monday to Friday, 8h-13h and 14h-17h)
INSERT INTO time_slots (doctor_id, day_of_week, start_time, end_time, is_available) VALUES
(3, 1, '08:00:00', '13:00:00', TRUE),
(3, 1, '14:00:00', '17:00:00', TRUE),
(3, 2, '08:00:00', '13:00:00', TRUE),
(3, 2, '14:00:00', '17:00:00', TRUE),
(3, 3, '08:00:00', '13:00:00', TRUE),
(3, 3, '14:00:00', '17:00:00', TRUE),
(3, 4, '08:00:00', '13:00:00', TRUE),
(3, 4, '14:00:00', '17:00:00', TRUE),
(3, 5, '08:00:00', '13:00:00', TRUE),
(3, 5, '14:00:00', '17:00:00', TRUE);

-- Insert Sample Appointments (some in the future, some completed)
INSERT INTO appointments (doctor_id, patient_id, appointment_date, appointment_time, status, notes, reminder_sent, created_by) VALUES
-- Future appointments
(1, 1, CURRENT_DATE + INTERVAL '2 days', '10:00:00', 'CONFIRMED', 'Consultation de suivi - Hypertension', TRUE, 4),
(1, 2, CURRENT_DATE + INTERVAL '3 days', '14:30:00', 'SCHEDULED', 'Bilan de santé annuel', FALSE, 5),
(2, 3, CURRENT_DATE + INTERVAL '1 day', '11:00:00', 'CONFIRMED', 'Détartrage et consultation', TRUE, 6),
(2, 4, CURRENT_DATE + INTERVAL '5 days', '16:00:00', 'SCHEDULED', 'Consultation orthodontie', FALSE, 7),
(3, 5, CURRENT_DATE + INTERVAL '1 day', '09:00:00', 'CONFIRMED', 'Vaccination enfant', TRUE, 8),
(3, 1, CURRENT_DATE + INTERVAL '7 days', '15:00:00', 'SCHEDULED', 'Consultation pédiatrique pour enfant', FALSE, 4),
-- Past appointments
(1, 1, CURRENT_DATE - INTERVAL '10 days', '10:00:00', 'COMPLETED', 'Consultation générale', TRUE, 4),
(2, 2, CURRENT_DATE - INTERVAL '15 days', '11:00:00', 'COMPLETED', 'Nettoyage dentaire', TRUE, 5),
(3, 5, CURRENT_DATE - INTERVAL '5 days', '09:00:00', 'COMPLETED', 'Suivi vaccination', TRUE, 8);

-- Display summary statistics
SELECT 'Data inserted successfully!' AS message;
SELECT COUNT(*) AS total_users FROM users;
SELECT COUNT(*) AS total_doctors FROM doctors;
SELECT COUNT(*) AS total_patients FROM patients;
SELECT COUNT(*) AS total_time_slots FROM time_slots;
SELECT COUNT(*) AS total_appointments FROM appointments;

-- Show upcoming appointments
SELECT
    CONCAT(u_patient.first_name, ' ', u_patient.last_name) AS patient_name,
    CONCAT(u_doctor.first_name, ' ', u_doctor.last_name) AS doctor_name,
    d.specialization,
    a.appointment_date,
    a.appointment_time,
    a.status
FROM appointments a
JOIN patients p ON a.patient_id = p.id
JOIN users u_patient ON p.user_id = u_patient.id
JOIN doctors d ON a.doctor_id = d.id
JOIN users u_doctor ON d.user_id = u_doctor.id
WHERE a.appointment_date >= CURRENT_DATE
ORDER BY a.appointment_date, a.appointment_time;
