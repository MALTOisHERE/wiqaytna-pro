import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import appointmentService from '../services/appointmentService';
import doctorService from '../services/doctorService';
import patientService from '../services/patientService';
import { getStatusLabel } from '../constants/statusTranslations';

/**
 * Patient Dashboard Component
 * Allows patients to search doctors and book appointments
 */
const PatientDashboard = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [patient, setPatient] = useState(null);
  const [appointments, setAppointments] = useState([]);
  const [doctors, setDoctors] = useState([]);
  const [searchKeyword, setSearchKeyword] = useState('');
  const [showBooking, setShowBooking] = useState(false);
  const [selectedDoctor, setSelectedDoctor] = useState(null);
  const [loading, setLoading] = useState(true);
  const [showDoctorDetails, setShowDoctorDetails] = useState(false);
  const [viewingDoctor, setViewingDoctor] = useState(null);
  const [showAppointments, setShowAppointments] = useState(true);
  const [showEditProfile, setShowEditProfile] = useState(false);
  const [collapsedSections, setCollapsedSections] = useState({
    SCHEDULED: false,
    CONFIRMED: false,
    COMPLETED: true,
    CANCELLED: true,
    NO_SHOW: true
  });
  const [editFormData, setEditFormData] = useState({
    dateOfBirth: '',
    bloodGroup: '',
    allergies: '',
    medicalHistory: '',
  });
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
      // First get patient record by user ID
      const patientData = await patientService.getPatientByUserId(user.id);
      setPatient(patientData);

      // Then fetch appointments using patient ID
      const appointmentsData = await appointmentService.getPatientUpcomingAppointments(patientData.id);
      setAppointments(appointmentsData);

      const doctorsData = await doctorService.getAllDoctors();
      setDoctors(doctorsData);
    } catch (error) {
      console.error('Error fetching data:', error);
    } finally {
      setLoading(false);
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
    if (!patient) {
      alert('Erreur: Informations patient non disponibles');
      return;
    }
    try {
      await appointmentService.createAppointment({
        doctorId: selectedDoctor.id,
        patientId: patient.id,
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

  const handleCancelAppointment = async (appointmentId) => {
    if (!window.confirm('Êtes-vous sûr de vouloir annuler ce rendez-vous?')) {
      return;
    }
    try {
      await appointmentService.cancelAppointment(appointmentId);
      alert('Rendez-vous annulé avec succès');
      fetchData();
    } catch (error) {
      alert(error.response?.data || 'Erreur lors de l\'annulation');
    }
  };

  const handleViewDoctorDetails = (doctor) => {
    setViewingDoctor(doctor);
    setShowDoctorDetails(true);
  };

  const handleEditProfile = () => {
    if (patient) {
      setEditFormData({
        dateOfBirth: patient.dateOfBirth || '',
        bloodGroup: patient.bloodGroup || '',
        allergies: patient.allergies || '',
        medicalHistory: patient.medicalHistory || '',
      });
      setShowEditProfile(true);
    }
  };

  const handleSubmitEditProfile = async (e) => {
    e.preventDefault();
    try {
      await patientService.updatePatient(patient.id, editFormData);
      alert('Profil mis à jour avec succès');
      setShowEditProfile(false);
      fetchData();
    } catch (error) {
      alert(error.response?.data || 'Erreur lors de la mise à jour du profil');
    }
  };

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const groupAppointmentsByStatus = () => {
    const grouped = {
      SCHEDULED: [],
      CONFIRMED: [],
      COMPLETED: [],
      CANCELLED: [],
      NO_SHOW: []
    };

    appointments.forEach(apt => {
      if (grouped[apt.status]) {
        grouped[apt.status].push(apt);
      }
    });

    // Sort by time within each group
    Object.keys(grouped).forEach(status => {
      grouped[status].sort((a, b) => {
        const dateA = new Date(a.appointmentDate + ' ' + a.appointmentTime);
        const dateB = new Date(b.appointmentDate + ' ' + b.appointmentTime);
        return dateA - dateB;
      });
    });

    return grouped;
  };

  const toggleSection = (status) => {
    setCollapsedSections(prev => ({
      ...prev,
      [status]: !prev[status]
    }));
  };

  if (loading) return <div className="loading">Chargement...</div>;

  return (
    <div className="dashboard">
      <header className="dashboard-header">
        <h1>Tableau de bord - Patient</h1>
        <div className="user-info">
          <span>{user.firstName} {user.lastName}</span>
          <button onClick={handleEditProfile} className="btn-primary" style={{ marginRight: '10px' }}>
            Modifier le profil
          </button>
          <button onClick={handleLogout} className="btn-secondary">Déconnexion</button>
        </div>
      </header>

      <div className="dashboard-content">
        <div className="search-section">
          <h2>Rechercher un médecin</h2>
          <div className="search-bar">
            <input
              type="text"
              placeholder="Nom, spécialité..."
              value={searchKeyword}
              onChange={(e) => setSearchKeyword(e.target.value)}
              onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
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
                <div style={{ display: 'flex', gap: '10px', marginTop: '15px' }}>
                  {doctor.bio && (
                    <button
                      onClick={() => handleViewDoctorDetails(doctor)}
                      className="btn-secondary"
                      style={{ flex: 1 }}>
                      Voir plus
                    </button>
                  )}
                  <button
                    onClick={() => handleBookAppointment(doctor)}
                    className="btn-primary"
                    style={{ flex: 1 }}>
                    Prendre RDV
                  </button>
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className="appointments-section" style={{ marginTop: '40px' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
            <h2>Mes rendez-vous à venir ({appointments.length})</h2>
            {appointments.length > 0 && (
              <button
                onClick={() => setShowAppointments(!showAppointments)}
                className="btn-secondary"
                style={{ width: 'auto', padding: '8px 16px' }}>
                {showAppointments ? 'Masquer' : 'Afficher'}
              </button>
            )}
          </div>
          {showAppointments && (
            appointments.length === 0 ? (
              <p>Aucun rendez-vous à venir</p>
            ) : (
              <div className="appointments-list-grouped">
                {Object.entries(groupAppointmentsByStatus()).map(([status, apts]) => (
                  <div key={status} className="status-group">
                    <div
                      className="status-group-header"
                      onClick={() => toggleSection(status)}>
                      <div className="status-group-title">
                        <span className={`status-badge status-${status.toLowerCase()}`}>
                          {getStatusLabel(status)}
                        </span>
                        <span className="status-group-count">({apts.length})</span>
                      </div>
                      <span className="collapse-icon">
                        {collapsedSections[status] ? '▼' : '▲'}
                      </span>
                    </div>
                    {!collapsedSections[status] && (
                      <div className="status-group-content">
                        {apts.length === 0 ? (
                          <div className="empty-status-group">Aucun rendez-vous</div>
                        ) : (
                          apts.map((apt) => (
                            <div key={apt.id} className="appointment-card">
                              <div className="appointment-header">
                                <h3>Dr. {apt.doctorName}</h3>
                              </div>
                              <p><strong>Spécialité:</strong> {apt.doctorSpecialization}</p>
                              <p><strong>Date:</strong> {new Date(apt.appointmentDate).toLocaleDateString('fr-FR')}</p>
                              <p><strong>Heure:</strong> {apt.appointmentTime}</p>
                              {apt.notes && <p><strong>Notes:</strong> {apt.notes}</p>}
                              {apt.status === 'CANCELLED' && apt.cancelledBy && (
                                <p style={{ color: '#ef4444', fontWeight: '500' }}>
                                  <strong>Annulé par:</strong> {apt.cancelledBy === 'DOCTOR' ? 'Le médecin' : 'Le patient'}
                                </p>
                              )}
                              {apt.status === 'SCHEDULED' && (
                                <div className="appointment-actions">
                                  <button
                                    onClick={() => handleCancelAppointment(apt.id)}
                                    className="btn-danger">
                                    Annuler
                                  </button>
                                </div>
                              )}
                            </div>
                          ))
                        )}
                      </div>
                    )}
                  </div>
                ))}
              </div>
            )
          )}
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

      {showDoctorDetails && viewingDoctor && (
        <div className="modal">
          <div className="modal-content">
            <h2>Dr. {viewingDoctor.firstName} {viewingDoctor.lastName}</h2>
            <div style={{ marginTop: '20px' }}>
              <p><strong>Spécialité:</strong> {viewingDoctor.specialization}</p>
              <p><strong>Cabinet:</strong> {viewingDoctor.cabinetAddress}</p>
              <p><strong>Expérience:</strong> {viewingDoctor.yearsOfExperience} ans</p>
              <p><strong>Tarif:</strong> {viewingDoctor.consultationFee} MAD</p>
              {viewingDoctor.bio && (
                <div style={{ marginTop: '15px', padding: '15px', backgroundColor: '#f5f7fa', borderRadius: '8px' }}>
                  <p><strong>À propos:</strong></p>
                  <p style={{ whiteSpace: 'pre-wrap', marginTop: '8px' }}>{viewingDoctor.bio}</p>
                </div>
              )}
            </div>
            <div className="modal-actions" style={{ marginTop: '20px' }}>
              <button
                onClick={() => {
                  setShowDoctorDetails(false);
                  handleBookAppointment(viewingDoctor);
                }}
                className="btn-primary">
                Prendre rendez-vous
              </button>
              <button onClick={() => setShowDoctorDetails(false)} className="btn-secondary">
                Fermer
              </button>
            </div>
          </div>
        </div>
      )}

      {showEditProfile && patient && (
        <div className="modal">
          <div className="modal-content">
            <h2>Modifier mon profil</h2>
            <form onSubmit={handleSubmitEditProfile}>
              <div className="form-group">
                <label>Date de naissance</label>
                <input
                  type="date"
                  value={editFormData.dateOfBirth}
                  onChange={(e) => setEditFormData({ ...editFormData, dateOfBirth: e.target.value })}
                />
              </div>
              <div className="form-group">
                <label>Groupe sanguin</label>
                <select
                  value={editFormData.bloodGroup}
                  onChange={(e) => setEditFormData({ ...editFormData, bloodGroup: e.target.value })}>
                  <option value="">Sélectionner...</option>
                  <option value="A+">A+</option>
                  <option value="A-">A-</option>
                  <option value="B+">B+</option>
                  <option value="B-">B-</option>
                  <option value="AB+">AB+</option>
                  <option value="AB-">AB-</option>
                  <option value="O+">O+</option>
                  <option value="O-">O-</option>
                </select>
              </div>
              <div className="form-group">
                <label>Allergies</label>
                <textarea
                  value={editFormData.allergies}
                  onChange={(e) => setEditFormData({ ...editFormData, allergies: e.target.value })}
                  rows="2"
                  placeholder="Allergies connues (médicaments, aliments, etc.)"
                />
              </div>
              <div className="form-group">
                <label>Antécédents médicaux</label>
                <textarea
                  value={editFormData.medicalHistory}
                  onChange={(e) => setEditFormData({ ...editFormData, medicalHistory: e.target.value })}
                  rows="3"
                  placeholder="Maladies chroniques, opérations précédentes, etc."
                />
              </div>
              <div className="modal-actions">
                <button type="submit" className="btn-primary">Enregistrer</button>
                <button type="button" onClick={() => setShowEditProfile(false)} className="btn-secondary">Annuler</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default PatientDashboard;
