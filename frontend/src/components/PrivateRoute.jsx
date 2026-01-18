import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

/**
 * Private Route Component
 * Protects routes that require authentication
 */
const PrivateRoute = ({ children, requiredRole }) => {
  const { user, isAuthenticated } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" />;
  }

  if (requiredRole && user.role !== requiredRole) {
    // Redirect to appropriate dashboard based on user role
    if (user.role === 'DOCTOR') {
      return <Navigate to="/doctor/dashboard" />;
    } else if (user.role === 'PATIENT') {
      return <Navigate to="/patient/dashboard" />;
    }
    return <Navigate to="/login" />;
  }

  return children;
};

export default PrivateRoute;
