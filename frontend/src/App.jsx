import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import Landing from './pages/Landing';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import DoctorDashboard from './pages/DoctorDashboard';
import PatientDashboard from './pages/PatientDashboard';
import PrivateRoute from './components/PrivateRoute';
import './styles/App.css';

/**
 * Main App component
 * Configures routing and authentication context
 */
function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          <Route path="/" element={<Landing />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />

          <Route
            path="/doctor/dashboard"
            element={
              <PrivateRoute requiredRole="DOCTOR">
                <DoctorDashboard />
              </PrivateRoute>
            }
          />

          <Route
            path="/patient/dashboard"
            element={
              <PrivateRoute requiredRole="PATIENT">
                <PatientDashboard />
              </PrivateRoute>
            }
          />
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;
