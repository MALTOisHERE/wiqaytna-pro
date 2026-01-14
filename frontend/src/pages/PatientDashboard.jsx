import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import appointmentService from '../services/appointmentService';
import doctorService from '../services/doctorService';
import { getStatusLabel } from '../constants/statusTranslations';

/**
 * Patient Dashboard Component
 * Allows patients to search doctors and book appointments
 */
const PatientDashboard = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [appointments, setAppointments] = useState([]);
  const [doctors, setDoctors] = useState([]);
  const [searchKeyword, setSearchKeyword] = useState('');
  const [showBooking, setShowBooking] = useState(false);
  const [selectedDoctor, setSelectedDoctor] = useState(null);
  const [bookingData, setBookingData] = useState({
    appointmentDate: '',
    appointmentTime: '',
    notes: '',
  });

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const appointmentsData = await appointmentService.getPatientUpcomingAppointments(user.id);
      setAppointments(appointmentsData);
      const doctorsData = await doctorService.getAllDoctors();
      setDoctors(doctorsData);
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };

  const handleSearch = async () => {
    try {
      const results = await doctorService.searchDoctors(searchKeyword);
      setDoctors(results);
    } catch (error) {
      console.error('Error searching:', error);
    }
  };

  const handleBookAppointment = (doctor) => {
    setSelectedDoctor(doctor);
    setShowBooking(true);
  };

  const handleSubmitBooking = async (e) => {
    e.preventDefault();
    try {
      await appointmentService.createAppointment({
        doctorId: selectedDoctor.id,
        patientId: user.id,
        ...bookingData,
      });
      alert('Rendez-vous créé avec succès!');
      setShowBooking(false);
      setBookingData({ appointmentDate: '', appointmentTime: '', notes: '' });
      fetchData();
    } catch (error) {
      alert(error.response?.data || 'Erreur lors de la création du rendez-vous');
    }
  };

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="dashboard">
      <header className="dashboard-header">
        <h1>Tableau de bord - Patient</h1>
        <div className="user-info">
          <span>{user.firstName} {user.lastName}</span>
          <button onClick={handleLogout} className="btn-secondary">Déconnexion</button>
        </div>
      </header>

      <div className="dashboard-content">
        <div className="appointments-section">
          <h2>Mes rendez-vous à venir ({appointments.length})</h2>
          {appointments.length === 0 ? (
            <p>Aucun rendez-vous à venir</p>
          ) : (
            <div className="appointments-list">
              {appointments.map((apt) => (
                <div key={apt.id} className="appointment-card">
                  <h3>Dr. {apt.doctorName}</h3>
                  <p><strong>Spécialité:</strong> {apt.doctorSpecialization}</p>
                  <p><strong>Date:</strong> {new Date(apt.appointmentDate).toLocaleDateString('fr-FR')}</p>
                  <p><strong>Heure:</strong> {apt.appointmentTime}</p>
                  <p><strong>Statut:</strong> <span className={`status-badge status-${apt.status.toLowerCase()}`}>{apt.status}</span></p>
                  {apt.notes && <p><strong>Notes:</strong> {apt.notes}</p>}
                </div>
              ))}
            </div>
          )}
        </div>

        <div className="search-section">
          <h2>Rechercher un médecin</h2>
          <div className="search-bar">
            <input
              type="text"
              placeholder="Nom, spécialité..."
              value={searchKeyword}
              onChange={(e) => setSearchKeyword(e.target.value)}
            />
            <button onClick={handleSearch} className="btn-primary">Rechercher</button>
          </div>
        </div>

        <div className="doctors-section">
          <h2>Médecins disponibles</h2>
          <div className="doctors-grid">
            {doctors.map((doctor) => (
              <div key={doctor.id} className="doctor-card">
                <h3>Dr. {doctor.firstName} {doctor.lastName}</h3>
                <p><strong>Spécialité:</strong> {doctor.specialization}</p>
                <p><strong>Cabinet:</strong> {doctor.cabinetAddress}</p>
                <p><strong>Expérience:</strong> {doctor.yearsOfExperience} ans</p>
                <p><strong>Tarif:</strong> {doctor.consultationFee} MAD</p>
                <button onClick={() => handleBookAppointment(doctor)} className="btn-primary">
                  Prendre rendez-vous
                </button>
              </div>
            ))}
          </div>
        </div>
      </div>

      {showBooking && (
        <div className="modal">
          <div className="modal-content">
            <h2>Prendre rendez-vous avec Dr. {selectedDoctor.firstName} {selectedDoctor.lastName}</h2>
            <form onSubmit={handleSubmitBooking}>
              <div className="form-group">
                <label>Date</label>
                <input
                  type="date"
                  value={bookingData.appointmentDate}
                  onChange={(e) => setBookingData({ ...bookingData, appointmentDate: e.target.value })}
                  min={new Date().toISOString().split('T')[0]}
                  required
                />
              </div>
              <div className="form-group">
                <label>Heure</label>
                <input
                  type="time"
                  value={bookingData.appointmentTime}
                  onChange={(e) => setBookingData({ ...bookingData, appointmentTime: e.target.value })}
                  required
                />
              </div>
              <div className="form-group">
                <label>Notes (optionnel)</label>
                <textarea
                  value={bookingData.notes}
                  onChange={(e) => setBookingData({ ...bookingData, notes: e.target.value })}
                  rows="3"
                />
              </div>
              <div className="modal-actions">
                <button type="submit" className="btn-primary">Confirmer</button>
                <button type="button" onClick={() => setShowBooking(false)} className="btn-secondary">Annuler</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default PatientDashboard;
