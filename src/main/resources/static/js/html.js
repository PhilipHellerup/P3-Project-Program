/**
 * HTML Utility Functions
 *
 * This module provides utility functions for handling job status display, translations,
 * and HTML text manipulation in the job management system.
 *
 * @module html
 */

/**
 * Maps human-readable status names to their corresponding database IDs.
 *
 * This object provides a bidirectional mapping between camelCase status names
 * used in JavaScript and the numeric IDs stored in the database.
 *
 * Status flow:
 * 1. notDelivered - Job has not been delivered yet
 * 2. delivered - Job has been delivered to shop
 * 3. inProgress - Work has started on the job
 * 4. missingPart - Job is waiting for a missing part
 * 5. finished - Work is complete
 * 6. pickedUp - Customer has picked up the completed job
 *
 * @constant {Object.<string, number>}
 */
// Status mappings
const statusNameToId = {
  notDelivered: 1,
  delivered: 2,
  inProgress: 3,
  missingPart: 4,
  finished: 5,
  pickedUp: 6,
};

/**
 * Reverse mapping from status IDs to their camelCase names.
 *
 * Automatically generated from statusNameToId by swapping keys and values.
 * Used to convert database numeric IDs back to JavaScript-friendly names.
 *
 * @constant {Object.<number, string>}
 * @example
 * statusIdToName[1] // returns 'notDelivered'
 * statusIdToName[3] // returns 'inProgress'
 */
const statusIdToName = Object.fromEntries(Object.entries(statusNameToId).map(([k, v]) => [v, k]));

/**
 * Returns color scheme and CSS class for a given job status.
 *
 * Each status has a distinct visual appearance with specific colors for:
 * - Background color (main element background)
 * - Border color (element borders/outlines)
 * - Text color (text displayed on the status badge)
 * - CSS class (for additional styling)
 *
 * Color scheme meanings:
 * - Yellow (notDelivered): Warning that job hasn't arrived
 * - Green (delivered): Positive confirmation of receipt
 * - Blue (inProgress): Active work indicator
 * - Pink/Red (missingPart): Alert for issue requiring attention
 * - Gray (finished): Neutral completion state
 * - Purple (pickedUp): Final state indicating closure
 *
 * @param {string} statusName - The camelCase status name (e.g., 'inProgress', 'finished')
 * @returns {Object} An object containing color values and CSS class
 * @returns {string} returns.backgroundColor - Hex color for element background
 * @returns {string} returns.borderColor - Hex color for element border
 * @returns {string} returns.textColor - Hex color for text (currently always '#000')
 * @returns {string} returns.cssClass - CSS class name for additional styling
 *
 * @example
 * const colors = statusToColors('inProgress');
 * // Returns: {
 * //   backgroundColor: '#4e73df',
 * //   borderColor: '#2e59d9',
 * //   textColor: '#000',
 * //   cssClass: 'status-inProgress'
 * // }
 */
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
      // Default to 'finished' appearance for unknown statuses
      return {
        backgroundColor: '#ffffff',
        borderColor: '#858796',
        textColor: '#858796',
        cssClass: 'status-finished',
      };
  }
}

/**
 * Escapes special HTML characters to prevent XSS attacks.
 *
 * This function is essential for security when injecting user-provided text
 * into HTML using innerHTML or similar methods. It converts potentially
 * dangerous characters into their HTML entity equivalents.
 *
 * Escaped characters:
 * - & becomes &amp;
 * - > becomes &gt;
 * - < becomes &lt;
 * - " becomes &quot;
 * - ' becomes &#39;
 *
 * @param {string|null|undefined} str - The string to escape
 * @returns {string} The escaped string, safe for insertion into HTML
 *
 * @example
 * escapeHtml('<script>alert("XSS")</script>')
 * // Returns: '&lt;script&gt;alert(&quot;XSS&quot;)&lt;/script&gt;'
 *
 * escapeHtml('John "The Boss" O\'Malley')
 * // Returns: 'John &quot;The Boss&quot; O&#39;Malley'
 *
 * escapeHtml(null)
 * // Returns: ''
 */
function escapeHtml(str) {
  if (str === null || str === undefined) return '';
  return String(str)
    .replace(/&/g, '&amp;')
    .replace(/>/g, '&gt;')
    .replace(/</g, '&lt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#39;');
}

/**
 * Converts camelCase or PascalCase strings to human-readable "Normal Case" format.
 *
 * This function takes programming-style naming conventions and converts them
 * to user-friendly display text by:
 * 1. Inserting spaces before capital letters
 * 2. Capitalizing the first letter of the result
 *
 * Useful for displaying enum values or variable names in the UI without
 * maintaining separate display name mappings.
 *
 * @param {string} status - The camelCase or PascalCase string to format
 * @returns {string} The formatted string with spaces and proper capitalization
 *
 * @example
 * formatStatusName('notDelivered')   // Returns: 'Not Delivered'
 * formatStatusName('inProgress')     // Returns: 'In Progress'
 * formatStatusName('missingPart')    // Returns: 'Missing Part'
 * formatStatusName('')               // Returns: ''
 * formatStatusName(null)             // Returns: ''
 */
function formatStatusName(status) {
  if (!status) return '';
  // Split camelCase or PascalCase into words and capitalize each
  return status
    .replace(/([a-z0-9])([A-Z])/g, '$1 $2') // split camelCase
    .replace(/^./, (s) => s.toUpperCase()); // capitalize first letter
}

/**
 * Translates job status from English (or numeric ID) to Danish.
 *
 * This function supports translation from two input formats:
 * 1. Numeric status IDs (1-6) from the database
 * 2. English camelCase status names from JavaScript code
 *
 * The function handles various input types and formats gracefully, returning
 * the original value if no translation is found.
 *
 * Status translations:
 * - notDelivered (1) → "Ikke Indleveret" (Not Delivered)
 * - delivered (2) → "Indleveret" (Delivered)
 * - inProgress (3) → "Igangværende" (In Progress)
 * - missingPart (4) → "Mangler Del" (Missing Part)
 * - finished (5) → "Færdig" (Finished)
 * - pickedUp (6) → "Afhentet" (Picked Up)
 *
 * @param {string|number|null|undefined} input - Status ID (number) or English name (string)
 * @returns {string} The Danish translation, or original value if no translation found
 *
 * @example
 * translateStatusName(1)               // Returns: 'Ikke Indleveret'
 * translateStatusName('3')             // Returns: 'Igangværende'
 * translateStatusName('inProgress')    // Returns: 'Igangværende'
 * translateStatusName('notDelivered')  // Returns: 'Ikke Indleveret'
 * translateStatusName('unknown')       // Returns: 'unknown'
 * translateStatusName(null)            // Returns: ''
 */
function translateStatusName(input) {
  if (input == null) return '';

  // Normalize input to string and trim whitespace
  const value = String(input).trim();

  // Check if input is a numeric ID (supports both number and string numbers)
  const id = Number.isFinite(+value) ? String(+value) : null;

  // Danish translations by numeric ID
  const byId = {
    1: 'Ikke Indleveret',
    2: 'Indleveret',
    3: 'Igangværende',
    4: 'Mangler del',
    5: 'Færdig',
    6: 'Afhentet',
  };

  // Danish translations by English camelCase name
  const byEn = {
    notDelivered: 'Ikke Indleveret',
    delivered: 'Indleveret',
    inProgress: 'Igangværende',
    missingPart: 'Mangler Del',
    finished: 'Færdig',
    pickedUp: 'Afhentet',
  };

  // Try ID lookup first, then English name lookup, finally return original value
  return (id && byId[id]) || byEn[value] || value;
}
