import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import appointmentService from '../services/appointmentService';
import doctorService from '../services/doctorService';
import { getStatusLabel, getStatusChangeMessage } from '../constants/statusTranslations';

/**
 * Doctor Dashboard Component
 * Displays doctor's appointments and schedule
 */
const DoctorDashboard = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [doctor, setDoctor] = useState(null);
  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDoctorData();
  }, []);

  const fetchDoctorData = async () => {
    try {
      const doctorData = await doctorService.getDoctorByUserId(user.id);
      setDoctor(doctorData);
      const appointmentsData = await appointmentService.getDoctorUpcomingAppointments(doctorData.id);
      setAppointments(appointmentsData);
    } catch (error) {
      console.error('Error fetching data:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const handleStatusChange = async (appointmentId, newStatus) => {
    try {
      await appointmentService.updateAppointmentStatus(appointmentId, newStatus);
      fetchDoctorData(); // Refresh data
    } catch (error) {
      console.error('Error updating status:', error);
      alert('Erreur lors de la mise à jour');
    }
  };

  if (loading) return <div className="loading">Chargement...</div>;

  return (
    <div className="dashboard">
      <header className="dashboard-header">
        <h1>Tableau de bord - Médecin</h1>
        <div className="user-info">
          <span>Dr. {user.firstName} {user.lastName}</span>
          <button onClick={handleLogout} className="btn-secondary">Déconnexion</button>
        </div>
      </header>

      <div className="dashboard-content">
        {doctor && (
          <div className="info-card">
            <h2>Mes informations</h2>
            <p><strong>Spécialité:</strong> {doctor.specialization}</p>
            <p><strong>Cabinet:</strong> {doctor.cabinetAddress}</p>
            <p><strong>Tarif:</strong> {doctor.consultationFee} MAD</p>
          </div>
        )}

        <div className="appointments-section">
          <h2>Rendez-vous à venir ({appointments.length})</h2>
          {appointments.length === 0 ? (
            <p>Aucun rendez-vous à venir</p>
          ) : (
            <div className="appointments-list">
              {appointments.map((apt) => (
                <div key={apt.id} className="appointment-card">
                  <div className="appointment-header">
                    <h3>{apt.patientName}</h3>
                    <span className={`status-badge status-${apt.status.toLowerCase()}`}>
                      {apt.status}
                    </span>
                  </div>
                  <p><strong>Date:</strong> {new Date(apt.appointmentDate).toLocaleDateString('fr-FR')}</p>
                  <p><strong>Heure:</strong> {apt.appointmentTime}</p>
                  <p><strong>Téléphone:</strong> {apt.patientPhone}</p>
                  {apt.notes && <p><strong>Notes:</strong> {apt.notes}</p>}
                  <div className="appointment-actions">
                    <button onClick={() => handleStatusChange(apt.id, 'CONFIRMED')} className="btn-success">Confirmer</button>
                    <button onClick={() => handleStatusChange(apt.id, 'COMPLETED')} className="btn-info">Terminé</button>
                    <button onClick={() => handleStatusChange(apt.id, 'CANCELLED')} className="btn-danger">Annuler</button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default DoctorDashboard;
