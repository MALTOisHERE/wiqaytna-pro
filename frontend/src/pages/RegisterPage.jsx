import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

/**
 * Register Page Component
 */
const RegisterPage = () => {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    role: 'PATIENT',
    firstName: '',
    lastName: '',
    phone: '',
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const navigate = useNavigate();
  const { register } = useAuth();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    try {
      await register(formData);
      setSuccess('Inscription réussie! Redirection vers la connexion...');
      setTimeout(() => navigate('/login'), 2000);
    } catch (err) {
      setError(err.response?.data || 'Inscription échouée');
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h1>Wiqaytna Pro</h1>
        <h2>Inscription</h2>
        {error && <div className="error-message">{error}</div>}
        {success && <div className="success-message">{success}</div>}
        <form onSubmit={handleSubmit}>
          <div className="form-row">
            <div className="form-group">
              <label>Prénom</label>
              <input type="text" name="firstName" value={formData.firstName} onChange={handleChange} required />
            </div>
            <div className="form-group">
              <label>Nom</label>
              <input type="text" name="lastName" value={formData.lastName} onChange={handleChange} required />
            </div>
          </div>
          <div className="form-group">
            <label>Email</label>
            <input type="email" name="email" value={formData.email} onChange={handleChange} required />
          </div>
          <div className="form-group">
            <label>Téléphone</label>
            <input type="tel" name="phone" value={formData.phone} onChange={handleChange} required />
          </div>
          <div className="form-group">
            <label>Mot de passe (min 8 caractères)</label>
            <input type="password" name="password" value={formData.password} onChange={handleChange} required minLength="8" />
          </div>
          <div className="form-group">
            <label>Type de compte</label>
            <select name="role" value={formData.role} onChange={handleChange}>
              <option value="PATIENT">Patient</option>
              <option value="DOCTOR">Médecin</option>
            </select>
          </div>
          <button type="submit" className="btn-primary">S'inscrire</button>
        </form>
        <p className="auth-link">
          Déjà un compte? <Link to="/login">Se connecter</Link>
        </p>
      </div>
    </div>
  );
};

export default RegisterPage;
