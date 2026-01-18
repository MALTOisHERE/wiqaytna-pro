import api from './api';

/**
 * Doctor Service
 * Handles doctor-related operations
 */
const doctorService = {
  /**
   * Get all doctors
   */
  getAllDoctors: async () => {
    const response = await api.get('/doctors');
    return response.data;
  },

  /**
   * Get doctor by ID
   */
  getDoctorById: async (doctorId) => {
    const response = await api.get(`/doctors/${doctorId}`);
    return response.data;
  },

  /**
   * Get doctor by user ID
   */
  getDoctorByUserId: async (userId) => {
    const response = await api.get(`/doctors/user/${userId}`);
    return response.data;
  },

  /**
   * Search doctors by keyword
   */
  searchDoctors: async (keyword) => {
    const response = await api.get('/doctors/search', {
      params: { keyword },
    });
    return response.data;
  },

  /**
   * Get doctors by specialization
   */
  getDoctorsBySpecialization: async (specialization) => {
    const response = await api.get('/doctors/specialization', {
      params: { name: specialization },
    });
    return response.data;
  },

  /**
   * Update doctor profile
   */
  updateDoctor: async (id, doctorData) => {
    const response = await api.put(`/doctors/${id}`, doctorData);
    return response.data;
  },
};

export default doctorService;
