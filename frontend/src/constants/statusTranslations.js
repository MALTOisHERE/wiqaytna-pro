/**
 * Appointment Status Translations
 * Maps English status codes to French labels
 */

export const APPOINTMENT_STATUS = {
  SCHEDULED: 'Planifié',
  CONFIRMED: 'Confirmé',
  CANCELLED: 'Annulé',
  COMPLETED: 'Terminé',
  NO_SHOW: 'Absence patient'
};

/**
 * Get French translation for appointment status
 * @param {string} status - English status code
 * @returns {string} French translation
 */
export const getStatusLabel = (status) => {
  return APPOINTMENT_STATUS[status] || status;
};

/**
 * Success messages for status changes
 */
export const STATUS_CHANGE_MESSAGES = {
  CONFIRMED: 'Rendez-vous confirmé avec succès',
  COMPLETED: 'Rendez-vous marqué comme terminé',
  CANCELLED: 'Rendez-vous annulé avec succès',
  NO_SHOW: 'Patient marqué comme absent'
};

/**
 * Get success message for status change
 * @param {string} status - New status
 * @returns {string} Success message
 */
export const getStatusChangeMessage = (status) => {
  return STATUS_CHANGE_MESSAGES[status] || 'Statut mis à jour avec succès';
};
