import api from './api';

/**
 * Patient Service
 * Handles patient-related API calls
 */
const patientService = {
  /**
   * Get all patients
   */
  getAllPatients: async () => {
    const response = await api.get('/patients');
    return response.data;
  },

  /**
   * Get patient by ID
   */
  getPatientById: async (id) => {
    const response = await api.get(`/patients/${id}`);
    return response.data;
  },

  /**
   * Get patient by user ID
   */
  getPatientByUserId: async (userId) => {
    const response = await api.get(`/patients/user/${userId}`);
    return response.data;
  },

  /**
   * Search patients by name
   */
  searchPatients: async (name) => {
    const response = await api.get('/patients/search', {
      params: { name },
    });
    return response.data;
  },

  /**
   * Update patient profile
   */
  updatePatient: async (id, patientData) => {
    const response = await api.put(`/patients/${id}`, patientData);
    return response.data;
  },
};

export default patientService;
