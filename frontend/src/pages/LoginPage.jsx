import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

/**
 * Login Page Component
 */
const LoginPage = () => {
  const [formData, setFormData] = useState({ email: '', password: '' });
  const [error, setError] = useState('');
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const response = await login(formData);
      // Redirect based on user role
      if (response.user.role === 'DOCTOR') {
        navigate('/doctor/dashboard');
      } else if (response.user.role === 'PATIENT') {
        navigate('/patient/dashboard');
      }
    } catch (err) {
      setError(err.response?.data || 'Login failed');
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h1>Wiqaytna Pro</h1>
        <h2>Connexion</h2>
        {error && <div className="error-message">{error}</div>}
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Email</label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <label>Mot de passe</label>
            <input
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              required
            />
          </div>
          <button type="submit" className="btn-primary">Se connecter</button>
        </form>
        <p className="auth-link">
          Pas de compte? <Link to="/register">S'inscrire</Link>
        </p>
      </div>
    </div>
  );
};

export default LoginPage;
