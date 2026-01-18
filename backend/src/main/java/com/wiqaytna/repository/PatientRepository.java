package com.wiqaytna.repository;

import com.wiqaytna.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Patient entity
 * Provides data access methods for patient operations
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * Find patient by user ID
     */
    Optional<Patient> findByUserId(Long userId);

    /**
     * Find all patients with their user information
     */
    @Query("SELECT p FROM Patient p JOIN FETCH p.user")
    List<Patient> findAllWithUser();

    /**
     * Find patients by blood group
     */
    List<Patient> findByBloodGroup(String bloodGroup);

    /**
     * Search patients by name
     */
    @Query("SELECT p FROM Patient p JOIN p.user u WHERE " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Patient> searchByName(String name);
}
