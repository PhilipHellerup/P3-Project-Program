// Imports
import { handleFetchErrors } from '/js/utils/fetchUtils.js';

document.addEventListener('DOMContentLoaded', function () {
    // Buttons for choosing type
    const addPartBtn = document.getElementById('addPartBtn');
    const addServiceBtn = document.getElementById('addServiceBtn');

    // Forms
    const choiceMenu = document.getElementById('choiceMenu');
    const productForm = document.getElementById('productForm');
    const serviceForm = document.getElementById('serviceForm');

    // Submit buttons
    const submitProductBtn = document.getElementById('submitProductBtn');
    const submitServiceBtn = document.getElementById('submitServiceBtn');

    // Show Product Form
    addPartBtn.addEventListener('click', () => {
        choiceMenu.classList.add('d-none');
        productForm.classList.remove('d-none');
        submitProductBtn.classList.remove('d-none');
    });

    // Show Service Form
    addServiceBtn.addEventListener('click', () => {
        choiceMenu.classList.add('d-none');
        serviceForm.classList.remove('d-none');
        submitServiceBtn.classList.remove('d-none');
    });

    // Product submission
    submitProductBtn.addEventListener('click', async function (e) {
        e.preventDefault();
        const productData = {
            productNumber: document.getElementById('varenummer-text').value,
            name: document.getElementById('navn-text').value,
            EAN: document.getElementById('EAN-text').value,
            type: document.getElementById('type-text').value,
            price: document.getElementById('pris-tal').value,
        };
        try {
            const response = await fetch('/api/products', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(productData),
            });
            await handleFetchErrors(response);
            window.location.reload();
        } catch (error) {
            console.error('Error creating product:', error);
        }
    });

    // Service submission
    submitServiceBtn.addEventListener('click', async function (e) {
        e.preventDefault();
        const serviceData = {
            name: document.getElementById('service-navn-text').value,
            price: document.getElementById('service-price').value,
            duration: document.getElementById('service-duration').value,
        };
        try {
            const response = await fetch('/api/services', {  // Make sure you have ServiceController
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(serviceData),
            });
            await handleFetchErrors(response);
            window.location.reload();
        } catch (error) {
            console.error('Error creating service:', error);
        }
    });
});




/*
// Imports
import { handleFetchErrors } from '/js/utils/fetchUtils.js';

// Handles the submission of the "Add Product" form on the product page.
// Sends product data to the server via POST request.

// Wait for the HTML DOM Content do be loaded and then run the function
document.addEventListener('DOMContentLoaded', function () {
    // Get the submit button by its unique ID
    const submitButton = document.getElementById('submitProductBtn');

    // Attach a click event listener to the submit button.
    // Using `async`, because it allows the use of `await` for the fetch call.
    submitButton.addEventListener('click', async function (e) {
        // Prevent the default form submission behavior (meaning that the page will now not reload on submission)
        e.preventDefault();

        // Collect all form input values into a product data object
        // Each property corresponds to an attribute in the database Product entity
        const productData = {
            productNumber: document.getElementById('varenummer-text').value, // Product Number
            name: document.getElementById('navn-text').value, // Product Name
            EAN: document.getElementById('EAN-text').value, // Product EAN
            type: document.getElementById('type-text').value, // Product Category/Type
            price: document.getElementById('pris-tal').value, // Product Price
        };

        try {
            // Send an asynchronous POST request to the server API endpoint ("/api/products") in "ProductController.java"
            // The `await` keyword pauses execution until the fetch completes
            const response = await fetch('/api/products', {
                method: 'POST', // HTTP method for creating new resources.
                headers: {
                    'Content-Type': 'application/json', // Tell the server that we are sending JSON data
                },
                body: JSON.stringify(productData), // Convert JavaScript object to JSON string
            });

            // Validate the response and throw specific error if needed
            await handleFetchErrors(response);

            // Success: Reload the table to show the new product
            window.location.reload();
        } catch (error) {
            // Catch any network errors or exceptions (e.g., server unreachable)
            console.error('Error creating product:', error); // Log the error details to the browser console
        }
    });
});
*/