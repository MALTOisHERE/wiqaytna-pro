package com.wiqaytna.repository;

import com.wiqaytna.model.User;
import com.wiqaytna.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity
 * Extends JpaRepository for CRUD operations
 * Follows Repository pattern - data access layer
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email (for login)
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if email already exists (for registration validation)
     */
    boolean existsByEmail(String email);

    /**
     * Find all users by role
     */
    List<User> findByRole(UserRole role);

    /**
     * Find all active users
     */
    List<User> findByIsActiveTrue();

    /**
     * Find user by phone number
     */
    Optional<User> findByPhone(String phone);
}
