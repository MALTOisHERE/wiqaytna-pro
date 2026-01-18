import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import appointmentService from '../services/appointmentService';
import doctorService from '../services/doctorService';
import { getStatusLabel, getStatusChangeMessage } from '../constants/statusTranslations';
import { FiPhone, FiUser, FiCalendar, FiClock, FiFileText, FiAlertCircle, FiDroplet, FiActivity } from 'react-icons/fi';

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
  const [viewMode, setViewMode] = useState('kanban'); // 'kanban' or 'list'
  const [showEditProfile, setShowEditProfile] = useState(false);
  const [collapsedSections, setCollapsedSections] = useState({
    SCHEDULED: false,
    CONFIRMED: false,
    COMPLETED: true,
    CANCELLED: true,
    NO_SHOW: true
  });
  const [editFormData, setEditFormData] = useState({
    specialization: '',
    cabinetAddress: '',
    consultationFee: '',
    yearsOfExperience: '',
    bio: '',
  });

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

  const handleStatusChange = async (appointmentId, newStatus, currentStatus) => {
    // Validate status transitions
    if (currentStatus === 'COMPLETED' || currentStatus === 'CANCELLED') {
      alert('Impossible de modifier un rendez-vous terminé ou annulé');
      return;
    }

    if (newStatus === 'COMPLETED' && currentStatus !== 'CONFIRMED') {
      if (!window.confirm('Le rendez-vous n\'est pas confirmé. Voulez-vous le marquer comme terminé?')) {
        return;
      }
    }

    // Special handling for cancellation
    if (newStatus === 'CANCELLED') {
      handleCancelAppointment(appointmentId);
      return;
    }

    try {
      await appointmentService.updateAppointmentStatus(appointmentId, newStatus);
      fetchDoctorData(); // Refresh data
      alert('Statut mis à jour avec succès');
    } catch (error) {
      console.error('Error updating status:', error);
      alert('Erreur lors de la mise à jour');
    }
  };

  const getAvailableActions = (status) => {
    switch (status) {
      case 'SCHEDULED':
        return ['CONFIRMED', 'CANCELLED'];
      case 'CONFIRMED':
        return ['COMPLETED', 'CANCELLED', 'NO_SHOW'];
      case 'COMPLETED':
      case 'CANCELLED':
      case 'NO_SHOW':
        return []; // No actions available for final states
      default:
        return [];
    }
  };

  const getActionButtonConfig = (action) => {
    const configs = {
      CONFIRMED: { label: 'Confirmer', className: 'btn-success' },
      COMPLETED: { label: 'Terminé', className: 'btn-info' },
      CANCELLED: { label: 'Annuler', className: 'btn-danger' },
      NO_SHOW: { label: 'Absence patient', className: 'btn-warning' },
    };
    return configs[action] || { label: action, className: 'btn-secondary' };
  };

  const handleCancelAppointment = async (appointmentId) => {
    if (!window.confirm('Êtes-vous sûr de vouloir annuler ce rendez-vous?')) {
      return;
    }
    try {
      await appointmentService.cancelAppointment(appointmentId, 'DOCTOR');
      fetchDoctorData();
      alert('Rendez-vous annulé avec succès');
    } catch (error) {
      alert('Erreur lors de l\'annulation');
    }
  };

  const handleEditProfile = () => {
    setEditFormData({
      specialization: doctor.specialization || '',
      cabinetAddress: doctor.cabinetAddress || '',
      consultationFee: doctor.consultationFee || '',
      yearsOfExperience: doctor.yearsOfExperience || '',
      bio: doctor.bio || '',
    });
    setShowEditProfile(true);
  };

  const handleSubmitEditProfile = async (e) => {
    e.preventDefault();
    try {
      await doctorService.updateDoctor(doctor.id, editFormData);
      alert('Profil mis à jour avec succès');
      setShowEditProfile(false);
      fetchDoctorData();
    } catch (error) {
      alert(error.response?.data || 'Erreur lors de la mise à jour du profil');
    }
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

  const renderKanbanCard = (apt) => {
    const availableActions = getAvailableActions(apt.status);

    return (
      <div key={apt.id} className="kanban-card">
        <div className="kanban-card-time">
          <FiCalendar style={{ marginRight: '6px' }} />
          {new Date(apt.appointmentDate).toLocaleDateString('fr-FR')}
          <FiClock style={{ marginLeft: '8px', marginRight: '4px' }} />
          {apt.appointmentTime}
        </div>
        <div className="kanban-card-patient">
          <FiUser style={{ marginRight: '6px' }} />
          {apt.patientName}
        </div>
        <div className="kanban-card-phone">
          <FiPhone style={{ marginRight: '6px' }} />
          {apt.patientPhone}
        </div>
        {apt.patientBloodGroup && (
          <div className="kanban-card-info">
            <FiDroplet style={{ marginRight: '6px' }} />
            Groupe sanguin: {apt.patientBloodGroup}
          </div>
        )}
        {apt.patientAllergies && (
          <div className="kanban-card-info alert">
            <FiAlertCircle style={{ marginRight: '6px' }} />
            Allergies: {apt.patientAllergies.length > 40 ? apt.patientAllergies.substring(0, 40) + '...' : apt.patientAllergies}
          </div>
        )}
        {apt.notes && (
          <div className="kanban-card-notes">
            <FiFileText style={{ marginRight: '6px' }} />
            {apt.notes.length > 50 ? apt.notes.substring(0, 50) + '...' : apt.notes}
          </div>
        )}
        {apt.status === 'CANCELLED' && apt.cancelledBy && (
          <div style={{ fontSize: '11px', color: '#ef4444', marginTop: '6px', fontWeight: '500' }}>
            Annulé par: {apt.cancelledBy === 'DOCTOR' ? 'Vous' : 'Le patient'}
          </div>
        )}
        {availableActions.length > 0 && (
          <div className="kanban-card-actions">
            {availableActions.map((action) => {
              const config = getActionButtonConfig(action);
              return (
                <button
                  key={action}
                  onClick={() => handleStatusChange(apt.id, action, apt.status)}
                  className={config.className}>
                  {config.label}
                </button>
              );
            })}
          </div>
        )}
      </div>
    );
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
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <h2>Mes informations</h2>
              <button onClick={handleEditProfile} className="btn-primary" style={{ width: 'auto', padding: '8px 16px' }}>
                Modifier le profil
              </button>
            </div>
            <p><strong>Spécialité:</strong> {doctor.specialization}</p>
            <p><strong>Cabinet:</strong> {doctor.cabinetAddress}</p>
            <p><strong>Tarif:</strong> {doctor.consultationFee} MAD</p>
            <p><strong>Expérience:</strong> {doctor.yearsOfExperience} ans</p>
            {doctor.bio && <p><strong>Bio:</strong> {doctor.bio}</p>}
          </div>
        )}

        <div className="appointments-section">
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
            <h2>Mes rendez-vous ({appointments.length})</h2>
            <div className="view-toggle">
              <button
                onClick={() => setViewMode('kanban')}
                className={viewMode === 'kanban' ? 'active btn-primary' : 'btn-secondary'}>
                Vue Kanban
              </button>
              <button
                onClick={() => setViewMode('list')}
                className={viewMode === 'list' ? 'active btn-primary' : 'btn-secondary'}>
                Vue Liste
              </button>
            </div>
          </div>

          {appointments.length === 0 ? (
            <p>Aucun rendez-vous à venir</p>
          ) : viewMode === 'kanban' ? (
            // Kanban View
            <div className="kanban-board">
              {Object.entries(groupAppointmentsByStatus()).map(([status, apts]) => (
                <div key={status} className={`kanban-column ${status.toLowerCase()}`}>
                  <div className="kanban-column-header">
                    <span className="kanban-column-title">{getStatusLabel(status)}</span>
                    <span className="kanban-column-count">{apts.length}</span>
                  </div>
                  <div className="kanban-cards">
                    {apts.length === 0 ? (
                      <div className="kanban-empty">Aucun rendez-vous</div>
                    ) : (
                      apts.map(apt => renderKanbanCard(apt))
                    )}
                  </div>
                </div>
              ))}
            </div>
          ) : (
            // List View - Grouped by Status
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
                        apts.map((apt) => {
                          const availableActions = getAvailableActions(apt.status);
                          return (
                            <div key={apt.id} className="appointment-card">
                              <div className="appointment-header">
                                <h3><FiUser style={{ marginRight: '8px' }} />{apt.patientName}</h3>
                              </div>
                              <p><FiCalendar style={{ marginRight: '8px' }} /><strong>Date:</strong> {new Date(apt.appointmentDate).toLocaleDateString('fr-FR')}</p>
                              <p><FiClock style={{ marginRight: '8px' }} /><strong>Heure:</strong> {apt.appointmentTime}</p>
                              <p><FiPhone style={{ marginRight: '8px' }} /><strong>Téléphone:</strong> {apt.patientPhone}</p>
                              {apt.patientBloodGroup && (
                                <p><FiDroplet style={{ marginRight: '8px', color: '#ef4444' }} /><strong>Groupe sanguin:</strong> {apt.patientBloodGroup}</p>
                              )}
                              {apt.patientAllergies && (
                                <p className="alert-info"><FiAlertCircle style={{ marginRight: '8px', color: '#f59e0b' }} /><strong>Allergies:</strong> {apt.patientAllergies}</p>
                              )}
                              {apt.patientMedicalHistory && (
                                <p><FiActivity style={{ marginRight: '8px' }} /><strong>Antécédents:</strong> {apt.patientMedicalHistory.length > 60 ? apt.patientMedicalHistory.substring(0, 60) + '...' : apt.patientMedicalHistory}</p>
                              )}
                              {apt.notes && <p><FiFileText style={{ marginRight: '8px' }} /><strong>Notes:</strong> {apt.notes}</p>}
                              {apt.status === 'CANCELLED' && apt.cancelledBy && (
                                <p style={{ color: '#ef4444', fontWeight: '500' }}>
                                  <strong>Annulé par:</strong> {apt.cancelledBy === 'DOCTOR' ? 'Vous' : 'Le patient'}
                                </p>
                              )}
                              {availableActions.length > 0 && (
                                <div className="appointment-actions">
                                  {availableActions.map((action) => {
                                    const config = getActionButtonConfig(action);
                                    return (
                                      <button
                                        key={action}
                                        onClick={() => handleStatusChange(apt.id, action, apt.status)}
                                        className={config.className}>
                                        {config.label}
                                      </button>
                                    );
                                  })}
                                </div>
                              )}
                            </div>
                          );
                        })
                      )}
                    </div>
                  )}
                </div>
              ))}
            </div>
          )}
        </div>
      </div>

      {showEditProfile && (
        <div className="modal">
          <div className="modal-content">
            <h2>Modifier le profil</h2>
            <form onSubmit={handleSubmitEditProfile}>
              <div className="form-group">
                <label>Spécialité *</label>
                <input
                  type="text"
                  value={editFormData.specialization}
                  onChange={(e) => setEditFormData({ ...editFormData, specialization: e.target.value })}
                  required
                />
              </div>
              <div className="form-group">
                <label>Adresse du cabinet *</label>
                <textarea
                  value={editFormData.cabinetAddress}
                  onChange={(e) => setEditFormData({ ...editFormData, cabinetAddress: e.target.value })}
                  rows="2"
                  required
                />
              </div>
              <div className="form-row">
                <div className="form-group">
                  <label>Tarif de consultation (MAD)</label>
                  <input
                    type="number"
                    value={editFormData.consultationFee}
                    onChange={(e) => setEditFormData({ ...editFormData, consultationFee: e.target.value })}
                    min="0"
                  />
                </div>
                <div className="form-group">
                  <label>Années d'expérience</label>
                  <input
                    type="number"
                    value={editFormData.yearsOfExperience}
                    onChange={(e) => setEditFormData({ ...editFormData, yearsOfExperience: e.target.value })}
                    min="0"
                  />
                </div>
              </div>
              <div className="form-group">
                <label>Bio (optionnel)</label>
                <textarea
                  value={editFormData.bio}
                  onChange={(e) => setEditFormData({ ...editFormData, bio: e.target.value })}
                  rows="3"
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

export default DoctorDashboard;
