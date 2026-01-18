package com.wiqaytna.repository;

import com.wiqaytna.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

/**
 * Repository interface for TimeSlot entity
 * Manages doctor availability schedules
 */
@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    /**
     * Find all time slots for a specific doctor
     */
    List<TimeSlot> findByDoctorId(Long doctorId);

    /**
     * Find available time slots for a doctor on a specific day
     */
    List<TimeSlot> findByDoctorIdAndDayOfWeekAndIsAvailableTrue(Long doctorId, Integer dayOfWeek);

    /**
     * Find all available time slots for a doctor
     */
    List<TimeSlot> findByDoctorIdAndIsAvailableTrue(Long doctorId);

    /**
     * Check if a time slot exists for a doctor at a specific time
     */
    @Query("SELECT CASE WHEN COUNT(ts) > 0 THEN true ELSE false END FROM TimeSlot ts WHERE " +
           "ts.doctorId = :doctorId AND ts.dayOfWeek = :dayOfWeek AND " +
           "((ts.startTime <= :startTime AND ts.endTime > :startTime) OR " +
           "(ts.startTime < :endTime AND ts.endTime >= :endTime) OR " +
           "(ts.startTime >= :startTime AND ts.endTime <= :endTime))")
    boolean existsOverlappingSlot(@Param("doctorId") Long doctorId,
                                   @Param("dayOfWeek") Integer dayOfWeek,
                                   @Param("startTime") LocalTime startTime,
                                   @Param("endTime") LocalTime endTime);

    /**
     * Find time slots by day of week
     */
    List<TimeSlot> findByDayOfWeek(Integer dayOfWeek);

    /**
     * Delete all time slots for a doctor
     */
    void deleteByDoctorId(Long doctorId);
}
