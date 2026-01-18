package com.wiqaytna.repository;

import com.wiqaytna.model.Appointment;
import com.wiqaytna.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Repository interface for Appointment entity
 * Manages appointment bookings and queries
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    /**
     * Find all appointments for a doctor
     */
    List<Appointment> findByDoctorId(Long doctorId);

    /**
     * Find all appointments for a patient
     */
    List<Appointment> findByPatientId(Long patientId);

    /**
     * Find appointments for a doctor on a specific date
     */
    List<Appointment> findByDoctorIdAndAppointmentDate(Long doctorId, LocalDate date);

    /**
     * Find appointments for a doctor within a date range
     */
    @Query("SELECT a FROM Appointment a WHERE a.doctorId = :doctorId AND " +
           "a.appointmentDate BETWEEN :startDate AND :endDate")
    List<Appointment> findByDoctorIdAndDateRange(@Param("doctorId") Long doctorId,
                                                   @Param("startDate") LocalDate startDate,
                                                   @Param("endDate") LocalDate endDate);

    /**
     * Find appointments by status
     */
    List<Appointment> findByStatus(AppointmentStatus status);

    /**
     * Find upcoming appointments for a patient
     */
    @Query("SELECT a FROM Appointment a WHERE a.patientId = :patientId AND " +
           "a.appointmentDate >= :today ORDER BY a.appointmentDate, a.appointmentTime")
    List<Appointment> findUpcomingAppointmentsByPatient(@Param("patientId") Long patientId,
                                                          @Param("today") LocalDate today);

    /**
     * Find upcoming appointments for a doctor
     */
    @Query("SELECT a FROM Appointment a WHERE a.doctorId = :doctorId AND " +
           "a.appointmentDate >= :today ORDER BY a.appointmentDate, a.appointmentTime")
    List<Appointment> findUpcomingAppointmentsByDoctor(@Param("doctorId") Long doctorId,
                                                         @Param("today") LocalDate today);

    /**
     * Check if appointment slot is already booked
     */
    boolean existsByDoctorIdAndAppointmentDateAndAppointmentTime(Long doctorId, LocalDate date, LocalTime time);

    /**
     * Find appointments that need reminders (24 hours before, not sent yet)
     */
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate = :tomorrow AND " +
           "a.reminderSent = false AND a.status IN ('SCHEDULED', 'CONFIRMED')")
    List<Appointment> findAppointmentsNeedingReminder(@Param("tomorrow") LocalDate tomorrow);

    /**
     * Find all appointments with doctor and patient details
     */
    @Query("SELECT a FROM Appointment a " +
           "JOIN FETCH a.doctor d " +
           "JOIN FETCH d.user " +
           "JOIN FETCH a.patient p " +
           "JOIN FETCH p.user")
    List<Appointment> findAllWithDetails();

    /**
     * Count appointments for a doctor by status
     */
    Long countByDoctorIdAndStatus(Long doctorId, AppointmentStatus status);

    /**
     * Count appointments for a patient by status
     */
    Long countByPatientIdAndStatus(Long patientId, AppointmentStatus status);
}
