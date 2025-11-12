/* --- Imports --- */
import { handleFetchErrors } from '/js/utils/fetchUtils.js';

// Wait until the entire DOM (HTML structure) has loaded before running the script
document.addEventListener('DOMContentLoaded', function () {
    /* --- SELECT DOM ELEMENTS --- */
    // Buttons for choosing which form to show
    const addPartBtn = document.getElementById('addPartBtn');       // Show product form
    const addServiceBtn = document.getElementById('addServiceBtn'); // Show service form

    // Forms for product and service
    const choiceMenu = document.getElementById('choiceMenu');   // Initial choice menu
    const productForm = document.getElementById('productForm'); // Product input form
    const serviceForm = document.getElementById('serviceForm'); // Service input form

    // Submit buttons inside the modal forms
    const submitProductBtn = document.getElementById('submitProductBtn');
    const submitServiceBtn = document.getElementById('submitServiceBtn');

    /* --- Back to Choice Menu Button --- */
    const backToChoiceBtn = document.getElementById('backToChoiceBtn');

    // Listen for a click on the back button
    backToChoiceBtn.addEventListener('click', () => {
        // Hide both forms
        productForm.classList.add('d-none');
        serviceForm.classList.add('d-none');

        // Show the choice menu
        choiceMenu.classList.remove('d-none');

        // Hide the submit buttons and back button
        submitProductBtn.classList.add('d-none');
        submitServiceBtn.classList.add('d-none');
        backToChoiceBtn.classList.add('d-none');

        // Reset both forms
        productForm.reset();
        serviceForm.reset();

        // Reset modal title
        modalTitle.textContent = "Tilføj Produkter";
    });

    /* --- Reset Modal Whenever it is Closed Down --- */
    // Modal Title
    const modalTitle = document.getElementById('modalTitle');

    // Listen for modal close down
    const addProductModal = document.getElementById('addProductModal');
    addProductModal.addEventListener('hidden.bs.modal', () => {
        // Show the choice menu
        choiceMenu.classList.remove('d-none');

        // Hide both forms
        productForm.classList.add('d-none');
        serviceForm.classList.add('d-none');

        // Hide the submit buttons and back button
        submitProductBtn.classList.add('d-none');
        submitServiceBtn.classList.add('d-none');
        backToChoiceBtn.classList.add('d-none');

        // Clear input values in the service and product form for clear state
        productForm.reset();
        serviceForm.reset();

        // Change modal title to fit the choice menu
        modalTitle.textContent = "Tilføj Produkter";
    });

    /* --- SHOW PRODUCT FORM --- */
    addPartBtn.addEventListener('click', () => {
        // Hide the choice menu
        choiceMenu.classList.add('d-none');

        // Show product form
        productForm.classList.remove('d-none');

        // Show product submit button
        submitProductBtn.classList.remove('d-none');

        // Show the back button
        backToChoiceBtn.classList.remove('d-none');

        // Change modal title to fit the Product Form
        modalTitle.textContent = "Tilføj Reservedel";
    });

    /* --- SHOW SERVICE FORM --- */
    addServiceBtn.addEventListener('click', () => {
        // Hide the choice menu
        choiceMenu.classList.add('d-none');

        // Show service form
        serviceForm.classList.remove('d-none');

        // Show service submit button
        submitServiceBtn.classList.remove('d-none');

        // Show the back button
        backToChoiceBtn.classList.remove('d-none');

        // Change modal title to fit the Service Form
        modalTitle.textContent = "Tilføj Ydelse";
    });

    /* --- PRODUCT SUBMISSION --- */
    submitProductBtn.addEventListener('click', async function (e) {
        // Prevent default form submission (no page reload)
        e.preventDefault();

        // Gather input values from the product form
        const productData = {
            name: document.getElementById('navn-text').value, // Product name
            EAN: document.getElementById('EAN-text').value,   // Product EAN code
            type: document.getElementById('type-text').value, // Product type/category
            price: document.getElementById('pris-tal').value, // Product price
        };

        try {
            // Send a POST request to create a new product in backend
            const response = await fetch('/api/products', { // Endpoint handled by ProductController
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(productData),
            });

            // Check for fetch errors and handle them gracefully
            await handleFetchErrors(response);

            // Reload the page to show the newly added product
            window.location.reload();
        }
        catch (error) {
            // Log any network/server errors
            console.error('Error creating product:', error);
        }
    });

    /* --- SERVICE SUBMISSION --- */
    submitServiceBtn.addEventListener('click', async function (e) {
        // Prevent default form submission (no page reload)
        e.preventDefault();

        // Gather input values from the service form
        const serviceData = {
            name: document.getElementById('service-navn-text').value,    // Service name
            price: document.getElementById('service-price').value,       // Service price
            duration: document.getElementById('service-duration').value, // Service duration in minutes
        };

        try {
            // Send a POST request to create a new service in backend
            const response = await fetch('/api/services', {  // Endpoint handled by ServiceController
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(serviceData),
            });

            // Check for fetch errors and handle them gracefully
            await handleFetchErrors(response);

            // Reload the page to show the newly added service
            window.location.reload();
        }
        catch (error) {
            // Log any network/server errors
            console.error('Error creating service:', error);
        }
    });
});
