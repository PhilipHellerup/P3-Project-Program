// Imports
import { handleFetchErrors } from '/js/utils/fetchUtils.js';

// Wait until the entire DOM (HTML structure) has loaded before running the script
document.addEventListener('DOMContentLoaded', () => {
  // Select all elements with the class "delete-btn" (trash can buttons)
  // and loop through them to attach an event listener
  document.querySelectorAll('.delete-btn').forEach((button) => {
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
          method: 'DELETE',
        });

        // Validate and throw specific error if necessary
        await handleFetchErrors(response);

        // Success: Remove product row
        row.remove();
      } catch (error) {
        // Catch any network or fetch-related errors and log them for debugging
        console.error('Error deleting product:', error);
      }
    });
  });
});
