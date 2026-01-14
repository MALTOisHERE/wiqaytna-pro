import api from './api';

/**
 * Appointment Service
 * Handles appointment booking and management
 */
const appointmentService = {
  /**
   * Create new appointment
   */
  createAppointment: async (appointmentData) => {
    const response = await api.post('/appointments', appointmentData);
    return response.data;
  },

  /**
   * Get appointments for a doctor
   */
  getDoctorAppointments: async (doctorId) => {
    const response = await api.get(`/appointments/doctor/${doctorId}`);
    return response.data;
  },

  /**
   * Get upcoming appointments for a doctor
   */
  getDoctorUpcomingAppointments: async (doctorId) => {
    const response = await api.get(`/appointments/doctor/${doctorId}/upcoming`);
    return response.data;
  },

  /**
   * Get appointments for a patient
   */
  getPatientAppointments: async (patientId) => {
    const response = await api.get(`/appointments/patient/${patientId}`);
    return response.data;
  },

  /**
   * Get upcoming appointments for a patient
   */
  getPatientUpcomingAppointments: async (patientId) => {
    const response = await api.get(`/appointments/patient/${patientId}/upcoming`);
    return response.data;
  },

  /**
   * Get appointments for a doctor on a specific date
   */
  getDoctorAppointmentsByDate: async (doctorId, date) => {
    const response = await api.get(`/appointments/doctor/${doctorId}/date`, {
      params: { date },
    });
    return response.data;
  },

  /**
   * Update appointment status
   */
  updateAppointmentStatus: async (appointmentId, status) => {
    const response = await api.put(`/appointments/${appointmentId}/status`, null, {
      params: { status },
    });
    return response.data;
  },

  /**
   * Cancel appointment
   */
  cancelAppointment: async (appointmentId) => {
    const response = await api.delete(`/appointments/${appointmentId}`);
    return response.data;
  },
};

export default appointmentService;
