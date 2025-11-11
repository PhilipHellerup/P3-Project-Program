// Imports
import { handleFetchErrors } from '/js/utils/fetchUtils.js';
import { parsePriceString } from '/js/utils/parsePrice.js';

/* --- DELETE PRODUCT --- */
// Wait until the entire DOM (HTML structure) has loaded before running the script
document.addEventListener('DOMContentLoaded', () => {
    // Select all elements with the class "delete-btn" in the product table (trash can buttons)
    // and loop through them to attach an event listener
    document.querySelectorAll('#productTable .delete-btn').forEach(button => {
        // Add a click event listener to each delete button
        button.addEventListener('click', async (e) => {
            // Prevent the default form submission behavior (meaning that the page will now not reload on submission)
            e.preventDefault();

            // Find the nearest table row (<tr>) element to the clicked delete button, the closest table row will be
            // the table row of which the desired product to delete will be in.
            const row = e.target.closest('tr');

            // Retrieve the product ID stored as a data attribute on the row
            const productId = row.getAttribute('data-id');

            // Safety Check: If no ID is found, log an error and stop execution
            if (!productId) {
                console.error('Missing product ID');
                return;
            }

            try {
                // Send a DELETE request to the backend API "ProductController.java" to remove the product
                const response = await fetch(`/api/products/${productId}`, {
                    method: 'DELETE'
                });

                // Validate and throw specific error if necessary
                await handleFetchErrors(response);

                // Success: Remove product row
                row.remove();

                // Success: Reload the page so table and pagination update automatically
                window.location.reload();
            }
            // Catch any network or fetch-related errors and log them for debugging
            catch (error) {
                console.error('Error deleting product:', error);
            }
        });
    });
});

/* --- EDIT PRODUCT (TOGGLE EDIT MODE) --- */
// Wait until the entire DOM (HTML structure) has loaded before running the script
document.addEventListener('DOMContentLoaded', () => {
    // Select all elements with the class "edit-btn" in the product table (pencil buttons)
    // and loop through them to attach an event listener
    document.querySelectorAll('#productTable .edit-btn').forEach(button => {
        // Add a click event listener to each edit button
        button.addEventListener('click', async (e) => {
            // Prevent page reload
            e.preventDefault();

            // Find the closest <tr> to the clicked button
            const row = e.target.closest('tr');

            // Get the product ID stored as a data attribute on the row
            const productId = row.getAttribute('data-id');

            // Safety Check: If no ID is found, log an error and stop execution
            if (!productId) {
                console.error('Missing product ID');
                return;
            }

            // Toggle edit mode: adds/removes the "editing" CSS class
            const isEditing = row.classList.toggle(`editing`) // Toggles CSS class

            // Select only the cells within that row that are editable (desired editable field = excluding ID)
            const cells = Array.from(row.querySelectorAll('td[data-field]')).filter(cell => cell.dataset.field !== 'productNumber');

            // Select the icon element inside the button for changing its text where specific text = specific symbol
            const editIcon = button.querySelector(`.edit-btn-icon`);

            // Check if the user is entering or exiting edit mode.
            if (isEditing) {
                /* --- ENTER EDIT MODE --- */
                // Visually indicate the row is editable
                cells.forEach(cell => cell.classList.add('table-warning'));

                // Change icon to indicate "save" to show the user can save changes
                editIcon.textContent = `save`;

                // Change button style to indicate active editing
                button.classList.replace(`btn-outline-dark`, `btn-dark`);

                // Enable editing for each editable cell
                cells.forEach(cell => {
                    // Make the cell content editable
                    cell.setAttribute('contenteditable', 'true')

                    // Store the original value in a data attribute for comparison later
                    cell.dataset.originalValue = cell.textContent;

                    // Listen for when the user finishes editing a cell (also called a "blur event")
                    cell.addEventListener('blur', async (ev) => {
                        // Get the field name from the cell's data-field attribute
                        const field = ev.target.getAttribute('data-field');

                        // Check if there is no data field on this cell, thus skipping the update
                        if (!field) {
                            console.warn('No data-field on this cell, skipping update.');
                            return;
                        }

                        // New value entered by the user
                        const newValue = ev.target.textContent;

                        // Old value before the change
                        const oldValue = ev.target.dataset.originalValue;

                        // Only send update/PUT request if the value is actually changed
                        if (newValue !== oldValue) {
                            // Check if the edited field is the price
                            if (field === 'price') {
                                // Convert the string input (which may contain commas, dots, currency symbols) to a proper float number
                                const parsed = parsePriceString(newValue);

                                // If the conversion fails (NaN = Not a Number), warn in console and do not send update
                                if (Number.isNaN(parsed)) {
                                    console.warn('Could not parse price string:', newValue);

                                    // Reset cell to original
                                    ev.target.textContent = oldValue;

                                    // Alert the user about the problem:
                                    alert("Price is not a number!")

                                    return;
                                }

                                // Send PUT request to backend to update the product's price with the parsed numeric value
                                await updateProduct(productId, { [field]: parsed });

                                // Display value immediately in EU format (e.g., "123,45 Kr.") as Performsport is in EU
                                ev.target.textContent = parsed.toLocaleString('de-DE', { // DE = Germany = EU
                                    minimumFractionDigits: 2,
                                    maximumFractionDigits: 2
                                }) + ' Kr.';

                                // Update the cell's original value reference to avoid sending duplicate updates later
                                ev.target.dataset.originalValue = ev.target.textContent; // Update reference value
                            }
                            // For all other fields (name, description, etc.), send the raw string value
                            else {
                                await updateProduct(productId, { [field]: newValue });

                                // Update the cell's original value reference to avoid sending duplicate updates later
                                ev.target.dataset.originalValue = newValue; // Update reference value
                            }
                        }
                    });
                });
            }
            else {
                /* --- EXIT EDIT MODE --- */
                // Remove visual highlight
                cells.forEach(cell => cell.classList.remove('table-warning'));
                cells.forEach(cell => cell.classList.remove('editable-cell'));

                // Change icon back to "edit"
                editIcon.textContent = 'edit_square';

                // Reset button style to normal
                button.classList.replace('btn-dark', 'btn-outline-dark');

                // Disable editing for all cells and remove original value data
                cells.forEach(cell => {
                    cell.removeAttribute('contenteditable'); // Removes editing ability
                    delete cell.dataset.originalValue; // Removing original value data
                });
            }
        });
    });
});

/* --- CONTROLLER CALL: PUT PRODUCT UPDATE --- */
// Sends a PUT request to update a product field in the backend "Product Controller"
/** @param {string} productId ID of the product to update **/
/** @param {Object} updatedData Object containing field(s) and value(s) to update **/
async function updateProduct(productId, updatedData) {
    try {
        // Send PUT request to the backend API
        const response = await fetch(`/api/products/${productId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(updatedData)
        });
        // Extra NOTES:
        // "method" HTTP Method: (Get, Post, Put, Delete)
        // "headers:" = How to interpret the data being sent
        //  - Content-Type = Tells Server what type of data is in the request body
        //  - application/json = The body is JSON, please parse it as JSON
        // "body:" = Converts JS object to JSON

        // Handle any fetch errors (network issues, server errors)
        await handleFetchErrors(response);
    }
    catch (error) {
        // Log error
        console.error('Error updating product:', error);
    }
}
