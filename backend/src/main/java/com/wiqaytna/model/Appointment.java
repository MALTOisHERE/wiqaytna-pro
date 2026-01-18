package com.wiqaytna.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Appointment entity - Core entity linking doctors and patients
 * Follows Single Responsibility: manages appointment scheduling data
 */
@Entity
@Table(name = "appointments",
       uniqueConstraints = @UniqueConstraint(columnNames = {"doctor_id", "appointment_date", "appointment_time"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Doctor ID is required")
    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", insertable = false, updatable = false)
    private Doctor doctor;

    @NotNull(message = "Patient ID is required")
    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", insertable = false, updatable = false)
    private Patient patient;

    @NotNull(message = "Appointment date is required")
    @FutureOrPresent(message = "Appointment date must be today or in the future")
    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @NotNull(message = "Appointment time is required")
    @Column(name = "appointment_time", nullable = false)
    private LocalTime appointmentTime;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "appointment_status")
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "reminder_sent")
    private Boolean reminderSent = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "cancelled_by")
    private String cancelledBy; // "PATIENT" or "DOCTOR"

    /**
     * Schedules the appointment
     */
    public void schedule() {
        this.status = AppointmentStatus.SCHEDULED;
    }

    /**
     * Confirms the appointment
     */
    public void confirm() {
        this.status = AppointmentStatus.CONFIRMED;
    }

    /**
     * Cancels the appointment
     */
    public void cancel() {
        this.status = AppointmentStatus.CANCELLED;
    }

    /**
     * Marks the appointment as completed
     */
    public void complete() {
        this.status = AppointmentStatus.COMPLETED;
    }

    /**
     * Marks the appointment as no-show
     */
    public void markAsNoShow() {
        this.status = AppointmentStatus.NO_SHOW;
    }

    /**
     * Marks reminder as sent
     */
    public void markReminderSent() {
        this.reminderSent = true;
    }

    /**
     * Checks if appointment is in the past
     */
    public boolean isPast() {
        LocalDateTime appointmentDateTime = LocalDateTime.of(appointmentDate, appointmentTime);
        return appointmentDateTime.isBefore(LocalDateTime.now());
    }

    /**
     * Checks if appointment is upcoming (within next 24 hours)
     */
    public boolean isUpcoming() {
        LocalDateTime appointmentDateTime = LocalDateTime.of(appointmentDate, appointmentTime);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusHours(24);
        return appointmentDateTime.isAfter(now) && appointmentDateTime.isBefore(tomorrow);
    }

    /**
     * Checks if appointment can be cancelled
     */
    public boolean isCancellable() {
        return !isPast() && (status == AppointmentStatus.SCHEDULED || status == AppointmentStatus.CONFIRMED);
    }
}
