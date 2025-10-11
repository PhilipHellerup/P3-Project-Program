// --- Status mappings ---
const statusNameToId = {
  notDelivered: 1,
  delivered: 2,
  inProgress: 3,
  missingPart: 4,
  finished: 5,
  pickedUp: 6,
};
const statusIdToName = Object.fromEntries(Object.entries(statusNameToId).map(([k, v]) => [v, k]));

function statusToColors(statusName) {
  switch (statusName) {
    case 'notDelivered':
      return {
        backgroundColor: '#f6c23e',
        borderColor: '#e0a800',
        textColor: '#000',
        cssClass: 'status-notDelivered',
      };
    case 'delivered':
      return {
        backgroundColor: '#1cc88a',
        borderColor: '#17a673',
        textColor: '#000',
        cssClass: 'status-delivered',
      };
    case 'inProgress':
      return {
        backgroundColor: '#4e73df',
        borderColor: '#2e59d9',
        textColor: '#000',
        cssClass: 'status-inProgress',
      };
    case 'missingPart':
      return {
        backgroundColor: '#f6b3b0',
        borderColor: '#e07a78',
        textColor: '#000',
        cssClass: 'status-missingPart',
      };
    case 'finished':
      return {
        backgroundColor: '#858796',
        borderColor: '#6c6c7a',
        textColor: '#000',
        cssClass: 'status-finished',
      };
    case 'pickedUp':
      return {
        backgroundColor: '#8e44ad',
        borderColor: '#732d91',
        textColor: '#000',
        cssClass: 'status-pickedUp',
      };
    default:
      return {
        backgroundColor: '#858796',
        borderColor: '#6c6c7a',
        textColor: '#000',
        cssClass: 'status-finished',
      };
  }
}

// simple HTML escaper for injecting text into innerHTML
function escapeHtml(str) {
  if (str === null || str === undefined) return '';
  return String(str)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#39;');
}

// Convert camelCase or PascalCase strings to "Normal Case"
function formatStatusName(status) {
  if (!status) return '';
  // Split camelCase or PascalCase into words and capitalize each
  return status
    .replace(/([a-z0-9])([A-Z])/g, '$1 $2') // split camelCase
    .replace(/^./, (s) => s.toUpperCase()); // capitalize first letter
}

function translateStatusName(input) {
  if (input == null) return '';

  // Normalize
  const value = String(input).trim();

  // Support both numeric IDs and English names
  const id = Number.isFinite(+value) ? String(+value) : null;

  const byId = {
    1: 'Ikke Indleveret',
    2: 'Indleveret',
    3: 'Igangværende',
    4: 'Mangler del',
    5: 'Færdig',
    6: 'Afhentet',
  };

  const byEn = {
    notDelivered: 'Ikke Indleveret',
    delivered: 'Indleveret',
    inProgress: 'Igangværende',
    missingPart: 'Mangler Del',
    finished: 'Færdig',
    pickedUp: 'Afhentet',
  };

  return (id && byId[id]) || byEn[value] || value;
}
