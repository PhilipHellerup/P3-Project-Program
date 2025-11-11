/* --- PRICE STRING TO NUMBER CONVERTER --- */
// Converts price string into a float number.
// Handles formats like
// - "123.45" (US decimal)
// - "123,45" (EU decimal)
// - "1,234.56" (US thousands separator)
// - "1.234,56" (EU thousands separator)
// - "123,45 Kr." (with currency text)
/** @param {string} raw String value of the price field **/
/** @returns {number} Parsed float value, or NaN if invalid **/
export function parsePriceString(raw) {
    // Return NaN if input is null or undefined
    if (raw === null || raw === undefined) return NaN;

    // Convert input to string and trim whitespace
    let s = String(raw).trim();

    // Remove currency symbols/text AND trailing dots (like "Kr." or "kr.")
    s = s.replace(/\s*(kr|kroner)\.?/gi, ''); // \s = whitespace, i = ignore case, g = global replace (replace all occurrences)

    // Remove all other non-digit, non-comma, non-dot characters
    s = s.replace(/[^\d.,]/g, '');

    // If String has a length of 0 return NaN = Not a Number
    if (s.length === 0) return NaN;

    // Determine separators
    const lastComma = s.lastIndexOf(',');
    const lastDot = s.lastIndexOf('.');

    if (lastComma !== -1 && lastDot !== -1) {
        // Both exist = Last one is decimal
        if (lastComma > lastDot) {
            // EU style: Last comma is decimal
            s = s.replace(/\./g, ''); // Remove dots (thousands)
            s = s.replace(',', '.'); // Convert decimal comma to dot
        } else {
            // US style: Last dot is decimal
            s = s.replace(/,/g, ''); // Remove commas (thousands)
        }
    } else if (lastComma !== -1) {
        // Only comma = Decimal
        s = s.replace(',', '.');
    }
    // Only dot or no separator = Use as decimal, nothing to do

    // Converted and cleaned string to float number
    return parseFloat(s);
}
