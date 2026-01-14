package com.wiqaytna.repository;

import com.wiqaytna.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Doctor entity
 * Provides custom queries for doctor-specific operations
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    /**
     * Find doctor by user ID
     */
    Optional<Doctor> findByUserId(Long userId);

    /**
     * Find doctors by specialization
     */
    List<Doctor> findBySpecializationContainingIgnoreCase(String specialization);

    /**
     * Find doctor by license number
     */
    Optional<Doctor> findByLicenseNumber(String licenseNumber);

    /**
     * Check if license number exists
     */
    boolean existsByLicenseNumber(String licenseNumber);

    /**
     * Find all doctors with their user information
     */
    @Query("SELECT d FROM Doctor d JOIN FETCH d.user")
    List<Doctor> findAllWithUser();

    /**
     * Search doctors by specialization or name
     */
    @Query("SELECT d FROM Doctor d JOIN d.user u WHERE " +
           "LOWER(d.specialization) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Doctor> searchDoctors(@Param("keyword") String keyword);

    /**
     * Find doctors by years of experience (minimum)
     */
    List<Doctor> findByYearsOfExperienceGreaterThanEqual(Integer years);
}
