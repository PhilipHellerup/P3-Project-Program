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
        backgroundColor: '#ffffff',
        borderColor: '#F65D60',
        textColor: '#F65D60',
        cssClass: 'status-notDelivered',
      };
    case 'delivered':
      return {
        backgroundColor: '#ffffff',
        borderColor: '#FEB568',
        textColor: '#FEB568',
        cssClass: 'status-delivered',
      };
    case 'inProgress':
      return {
        backgroundColor: '#ffffff',
        borderColor: '#0088FF',
        textColor: '#0088FF',
        cssClass: 'status-inProgress',
      };
    case 'missingPart':
      return {
        backgroundColor: '#ffffff',
        borderColor: '#4B41C8',
        textColor: '#4B41C8',
        cssClass: 'status-missingPart',
      };
    case 'finished':
      return {
        backgroundColor: '#ffffff',
        borderColor: '#32A759',
        textColor: '#32A759',
        cssClass: 'status-finished',
      };
    case 'pickedUp':
      return {
        backgroundColor: '#ffffff',
        borderColor: '#939292',
        textColor: '#939292',
        cssClass: 'status-pickedUp',
      };
    default:
      return {
        backgroundColor: '#ffffff',
        borderColor: '#858796',
        textColor: '#858796',
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
