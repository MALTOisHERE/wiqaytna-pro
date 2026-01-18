-- Migration script to add cancelled_by column to appointments table
-- Run this script after updating the code

-- Add cancelled_by column to track who cancelled the appointment
ALTER TABLE appointments
ADD COLUMN IF NOT EXISTS cancelled_by VARCHAR(20);

-- Add comment to explain the column
COMMENT ON COLUMN appointments.cancelled_by IS 'Tracks who cancelled the appointment: PATIENT or DOCTOR';
