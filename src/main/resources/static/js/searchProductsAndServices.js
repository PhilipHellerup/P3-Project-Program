// Wrapped in an IIFE to avoid pollution
(function() {

    // Debounce delay for input events - optimization so that the search only happens every 250 ms instead of every keystroke
    const DEBOUNCE_MS = 250;
    let timer = null;

    // Run when the DOM is fully loaded
    document.addEventListener('DOMContentLoaded', () => {

        // Get search input and table body elements (product or service)
        const input = document.getElementById('search-input');
        const product_tbody = document.getElementById('productTable-body');
        const service_tbody = document.getElementById('serviceTable-body');

        // If no search input exists, stop script
        if (!input) return;

        // Update placeholder text based on which table is shown on the page
        if (product_tbody) {
            input.placeholder = "Søg efter reservedele"; // Search spare parts
        } else if (service_tbody) {
            input.placeholder = "Søg efter ydelser";     // Search services
        }

        // Executes the filtering immediately - Reads the search query, hides rows that do not match, and refreshes pagination (if implemented).
        const filterNow = () => {
            const q = (input.value || '').trim().toLowerCase();
            let rows;

            // Product table search
            if (product_tbody) {
                rows = product_tbody.querySelectorAll('tr');

                // If search is empty → show all rows and refresh pagination
                if (q === "") {
                    rows.forEach(row => row.classList.remove('d-none'));
                    if (window.Pagination) window.Pagination.refresh();
                    return;
                }

                // Filter each row by productNumber, name, EAN, or type
                rows.forEach(row => {
                    const pn   = row.querySelector('td[data-field="productNumber"]')?.textContent?.toLowerCase() || '';
                    const name = row.querySelector('td[data-field="name"]')?.textContent?.toLowerCase() || '';
                    const ean  = row.querySelector('td[data-field="EAN"]')?.textContent?.toLowerCase() || '';
                    const type = row.querySelector('td[data-field="type"]')?.textContent?.toLowerCase() || '';

                    const matches =
                        pn.includes(q) ||
                        name.includes(q) ||
                        ean.includes(q) ||
                        type.includes(q);

                    // Hide or show row based on match
                    row.classList.toggle('d-none', !matches);
                });

                // Recalculate pagination so it fits filtered rows
                if (window.Pagination) window.Pagination.refresh();
            }

            // Service table search
            else if (service_tbody) {
                rows = service_tbody.querySelectorAll('tr');

                // If search is empty → show all rows and refresh pagination
                if (q === "") {
                    rows.forEach(row => row.classList.remove('d-none'));
                    if (window.Pagination) window.Pagination.refresh();
                    return;
                }

                // Filter each row by id, name, price, or duration
                rows.forEach(row => {
                    const sn   = row.querySelector('td[data-field="id"]')?.textContent?.toLowerCase() || '';
                    const name = row.querySelector('td[data-field="name"]')?.textContent?.toLowerCase() || '';
                    const price = row.querySelector('td[data-field="price"]')?.textContent?.toLowerCase() || '';
                    const dura  = row.querySelector('td[data-field="duration"]')?.textContent?.toLowerCase() || '';

                    const matches =
                        sn.includes(q) ||
                        name.includes(q) ||
                        price.includes(q) ||
                        dura.includes(q);

                    // Hide or show row based on match
                    row.classList.toggle('d-none', !matches);
                });

                // Refresh pagination
                if (window.Pagination) window.Pagination.refresh();
            }
        };

        // The debouncedFilter to avoid filtering on every keystroke but instead every 250ms. Delay time defined in the top of this file
        const debouncedFilter = () => {
            clearTimeout(timer);
            timer = setTimeout(filterNow, DEBOUNCE_MS);
        };

        // Trigger filtering on user input
        input.addEventListener('input', debouncedFilter);

        // Prevent form submit from reloading the page
        const searchForm = input.closest('form');
        if (searchForm) {
            searchForm.addEventListener('submit', (e) => {
                e.preventDefault();
            });
        }
    });
})();
