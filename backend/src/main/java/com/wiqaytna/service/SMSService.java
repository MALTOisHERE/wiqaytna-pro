package com.wiqaytna.service;

import com.wiqaytna.model.Appointment;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

/**
 * SMS Service for sending notifications
 * Mock implementation - logs to console instead of sending real SMS
 * Can be upgraded to use real SMS API (e.g., Octopush) in production
 */
@Service
public class SMSService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Send appointment confirmation SMS
     */
    public void sendAppointmentConfirmation(Appointment appointment) {
        String patientName = getPatientName(appointment);
        String patientPhone = getPatientPhone(appointment);
        String doctorName = getDoctorName(appointment);
        String date = appointment.getAppointmentDate().format(DATE_FORMATTER);
        String time = appointment.getAppointmentTime().format(TIME_FORMATTER);

        String message = String.format(
                "Confirmation Wiqaytna Pro: Rendez-vous confirmé le %s à %s avec Dr. %s. ID: %d",
                date, time, doctorName, appointment.getId()
        );

        sendSMS(patientPhone, message, "CONFIRMATION");
    }

    /**
     * Send appointment reminder SMS (24 hours before)
     */
    public void sendReminder(Appointment appointment) {
        String patientPhone = getPatientPhone(appointment);
        String doctorName = getDoctorName(appointment);
        String specialization = getDoctorSpecialization(appointment);
        String cabinetAddress = getCabinetAddress(appointment);
        String date = appointment.getAppointmentDate().format(DATE_FORMATTER);
        String time = appointment.getAppointmentTime().format(TIME_FORMATTER);

        String message = String.format(
                "Rappel Wiqaytna Pro: Rendez-vous demain %s à %s avec Dr. %s (%s). Cabinet: %s. Pour annuler, appelez le cabinet.",
                date, time, doctorName, specialization, cabinetAddress
        );

        sendSMS(patientPhone, message, "REMINDER");
    }

    /**
     * Send cancellation notification
     */
    public void sendCancellationNotification(Appointment appointment) {
        String patientPhone = getPatientPhone(appointment);
        String doctorName = getDoctorName(appointment);
        String date = appointment.getAppointmentDate().format(DATE_FORMATTER);
        String time = appointment.getAppointmentTime().format(TIME_FORMATTER);

        String message = String.format(
                "Annulation Wiqaytna Pro: Votre rendez-vous du %s à %s avec Dr. %s a été annulé.",
                date, time, doctorName
        );

        sendSMS(patientPhone, message, "CANCELLATION");
    }

    /**
     * Mock SMS sending method
     * In production, replace with actual SMS API call (Octopush, Twilio, etc.)
     */
    private void sendSMS(String phoneNumber, String message, String type) {
        System.out.println("\n================================");
        System.out.println("[SMS MOCK - " + type + "]");
        System.out.println("To: " + phoneNumber);
        System.out.println("Message: " + message);
        System.out.println("Timestamp: " + java.time.LocalDateTime.now());
        System.out.println("================================\n");

        // TODO: In production, implement actual SMS API integration:
        // - Register with SMS provider (Octopush, Twilio, etc.)
        // - Add API credentials to application.properties
        // - Make HTTP POST request to SMS API
        // - Handle response and errors
    }

    /**
     * Helper methods to extract information from appointment
     */
    private String getPatientName(Appointment appointment) {
        if (appointment.getPatient() != null && appointment.getPatient().getUser() != null) {
            return appointment.getPatient().getUser().getFullName();
        }
        return "Patient";
    }

    private String getPatientPhone(Appointment appointment) {
        if (appointment.getPatient() != null && appointment.getPatient().getUser() != null) {
            return appointment.getPatient().getUser().getPhone();
        }
        return "+212600000000";
    }

    private String getDoctorName(Appointment appointment) {
        if (appointment.getDoctor() != null && appointment.getDoctor().getUser() != null) {
            return appointment.getDoctor().getUser().getFullName();
        }
        return "Docteur";
    }

    private String getDoctorSpecialization(Appointment appointment) {
        if (appointment.getDoctor() != null) {
            return appointment.getDoctor().getSpecialization();
        }
        return "Médecin";
    }

    private String getCabinetAddress(Appointment appointment) {
        if (appointment.getDoctor() != null) {
            return appointment.getDoctor().getCabinetAddress();
        }
        return "Cabinet médical";
    }
}
