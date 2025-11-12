import { attachProductActions } from './productActions.js'; // Makes Product Actions Buttons Work (Delete & Edit)
import { attachServiceActions } from './serviceActions.js'; // Makes Service Actions Buttons Work (Delete & Edit)

// Wait until the full HTML DOM has been loaded before executing
document.addEventListener("DOMContentLoaded", () => {
    // The search form and input field
    const searchForm = document.querySelector(".search-bar form");
    const searchInput = searchForm?.querySelector("input[type='search']");
    // NOTE FOR EXAM:
    // The `?.` is the optional chaining operator in JavaScript. It means:
    // If searchForm is not null or undefined, then call .querySelector(...); otherwise, return undefined instead of throwing an error.

    // References to product/service table bodies
    const productTableBody = document.querySelector("#productTable tbody");
    const serviceTableBody = document.querySelector("#serviceTable tbody");

    // References to table containers to check which tab is active
    const productTableContainer = document.querySelector("#productTableContainer");
    const serviceTableContainer = document.querySelector("#serviceTableContainer");

    // If the search form or input is missing, stop execution
    if (!searchForm || !searchInput) return;

    // Listen for form submission (search triggered by pressing Enter or clicking search button)
    searchForm.addEventListener("submit", async (event) => {
        // Prevent default form submission reload
        event.preventDefault();

        // Trim the search input value
        const query = searchInput.value.trim();

        // Detect which tab is currently active (product or service)
        const isProductTab = productTableContainer && !productTableContainer.classList.contains("d-none");
        const isServiceTab = serviceTableContainer && !serviceTableContainer.classList.contains("d-none");

        // Fetch Products Or Services
        try {
            // Fetch products matching the query and update the table with pagination
            if (isProductTab) {
                const products = await fetchProducts(query);

                // Limit to 7 rows per page
                setupPagination(productTableBody, products, 7); // Attaches actions buttons inside displayPage()

            }
            else if (isServiceTab) {
                // Fetch services matching the query and update the table with pagination
                const services = await fetchServices(query);

                // Limit to 7 rows per page
                setupPagination(serviceTableBody, services, 7); // Attaches actions buttons inside displayPage()

            }
        }
        catch (error) {
            console.error("Search failed:", error);
        }
    });

    // Fetch matching products from the backend API (SearchController)
    async function fetchProducts(query) {
        // Send fetch request
        const res = await fetch(`/api/search/parts?q=${encodeURIComponent(query)}`);

        // Returns array of part/Product objects
        return await res.json();
    }

    // Fetch matching services from the backend API (SearchController)
    async function fetchServices(query) {
        // Send fetch request
        const res = await fetch(`/api/search/service?q=${encodeURIComponent(query)}`);

        // Returns array of Service objects
        return await res.json();
    }

    // Dynamically populate the table with rows and handle pagination
    /** @param {HTMLElement} tableBody = <tbody> element of the table **/
    /** @param {Array} items = List of products or services **/
    /** @param {number} rowsPerPage = Maximum number of rows visible per page **/
    function setupPagination(tableBody, items, rowsPerPage) {
        // Clear any previous table content
        tableBody.innerHTML = "";

        // If no results, show a "no results" message and clear pagination
        if (!items.length) {
            // Determine the number of columns in the table so the "No Results" row spans all columns
            const colSpan = tableBody.closest("table").querySelectorAll("thead th").length;

            // Show the "No Results" message spanning the whole table width
            tableBody.innerHTML = `<tr><td colspan="${colSpan}" class="text-center text-muted py-3">No Matching Results :(</td></tr>`;

            // Clear Pagination
            document.querySelector(".pagination-css .pagination").innerHTML = "";

            // Return nothing
            return;
        }

        // Create <tr> elements for each item
        const rows = items.map(item => {
            // Create table row element
            const row = document.createElement("tr");

            // Store ID for potential delete/edit actions
            row.setAttribute("data-id", item.id);

            // Product Row
            if (item.ean !== undefined) { // Product
                row.innerHTML = `
                    <td class="fs-5">${item.id}</td>
                    <td class="fs-5">${item.name}</td>
                    <td class="fs-5">${item.ean || ""}</td>
                    <td class="fs-5">${item.type || ""}</td>
                    <td class="fs-5">${item.price?.toFixed(2) || "0.00"} Kr.</td>
                    <td class="fs-5">
                        <button class="btn btn-outline-danger btn-sm delete-btn" type="button">
                            <span class="material-symbols-outlined">delete</span>
                        </button>
                        <button class="btn btn-outline-dark btn-sm edit-btn" type="button">
                            <span class="material-symbols-outlined">edit_square</span>
                        </button>
                    </td>
                `;
            }
            // Service Row
            else { // Service
                row.innerHTML = `
                    <td class="fs-5">${item.id}</td>
                    <td class="fs-5">${item.name}</td>
                    <td class="fs-5">${item.price?.toFixed(2) || "0.00"} Kr.</td>
                    <td class="fs-5">${item.duration || ""}</td>
                    <td class="fs-5">
                        <button class="btn btn-outline-danger btn-sm delete-btn" type="button">
                            <span class="material-symbols-outlined">delete</span>
                        </button>
                        <button class="btn btn-outline-dark btn-sm edit-btn" type="button">
                            <span class="material-symbols-outlined">edit_square</span>
                        </button>
                    </td>
                `;
            }
            return row;
        });

        // Reference to the Pagination Container & Logic
        const paginationContainer = document.querySelector(".pagination-css .pagination");

        // clear old pagination buttons
        paginationContainer.innerHTML = ""; // clear old pagination

        // Keep track of the active page
        let currentPage = 1;

        // Calculate total pages
        const totalPages = Math.ceil(rows.length / rowsPerPage);

        // Display rows for a given page number
        function displayPage(page) {
            // Clear the current table body
            tableBody.innerHTML = "";

            // Calculate which rows should be visible for this page
            const start = (page - 1) * rowsPerPage; // Start index (inclusive)
            const end = start + rowsPerPage;        // End index (exclusive)

            // Add rows for the current page
            rows.slice(start, end).forEach(r => tableBody.appendChild(r));

            // Update current page
            currentPage = page;

            // After rows are in the table, attach the product/service action listeners
            if (tableBody.id === "productTable") {
                attachProductActions(); // <-- Attach delete/edit for products
            } else if (tableBody.id === "serviceTable") {
                attachServiceActions(); // <-- Attach delete/edit for services
            }

            // Refresh pagination buttons
            updatePaginationButtons();
        }

        // Generate and highlight pagination buttons dynamically
        function updatePaginationButtons() {
            // Clear buttons
            paginationContainer.innerHTML = "";

            /* --- Previous Page Button --- */
            // Create list-element
            const prevItem = document.createElement("li");

            // Add page-item class
            prevItem.classList.add("page-item");

            // Give it the icon
            prevItem.innerHTML = `<a class="page-link" href="#"><span class="material-symbols-outlined">chevron_left</span></a>`;

            // Listen for click
            prevItem.addEventListener("click", e => {
                e.preventDefault(); // Prevent reload

                // Check if the current page is below page 1, thus not possible
                if (currentPage > 1) {
                    displayPage(currentPage - 1);
                }
            });

            // Add button/list-element to the pagination list
            paginationContainer.appendChild(prevItem);

            /* --- Numbered Page Buttons --- */
            // Create numbered page buttons dynamically according to how many rows in table
            for (let i = 1; i <= totalPages; i++) {
                // Create list-element
                const li = document.createElement("li");

                // Add page-item class
                li.classList.add("page-item");

                // Give current page active status
                if (i === currentPage) {
                    li.classList.add("active");
                }

                // Give number to button
                li.innerHTML = `<a class="page-link" href="#">${i}</a>`;

                li.addEventListener("click", e => {
                    e.preventDefault(); // Prevent reload

                    // Display page matching the number
                    displayPage(i);
                });

                // Add button/list-element to the pagination list
                paginationContainer.appendChild(li);
            }

            /* --- Next Page Button --- */
            // Create list-element
            const nextItem = document.createElement("li");

            // Create list-element
            nextItem.classList.add("page-item");

            // Give it the icon
            nextItem.innerHTML = `<a class="page-link" href="#"><span class="material-symbols-outlined">chevron_right</span></a>`;

            // Listen for click
            nextItem.addEventListener("click", e => {
                e.preventDefault(); // Prevent reload

                // Only display the next page if the current page is lower than total pages and thus possible
                if (currentPage < totalPages) {
                    displayPage(currentPage + 1);
                }
            });

            // Add button/list-element to the pagination list
            paginationContainer.appendChild(nextItem);
        }

        // Initialize the table by displaying the first page
        displayPage(1);
    }
});
