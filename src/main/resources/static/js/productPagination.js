// Handles client-side pagination for the product table
// (Limit displayed rows to 7 per page and enables switching between pages)

// Wait for the HTML DOM Content do be loaded and then run the function
document.addEventListener('DOMContentLoaded', () => {
  // Configuration & Initialization
  // Max number or product rows visible per page
  const rowsPerPage = 7;

  // Table body containing the product rows
  const tableBody = document.querySelector('table tbody');

  // Converts the list of <tr> (table row) elements into an array
  const rows = Array.from(tableBody.querySelectorAll('tr'));

  // UL (Unordered List) element for pagination buttons (look in "products.html")
  const paginationContainer = document.querySelector('.pagination');

  // Tracks the currently active page number
  let currentPage = 1; // track current page

  // Functions
  // Display the selected page
  function displayPage(pageNumber) {
    // Calculate how many total pages are needed
    const totalPages = Math.ceil(rows.length / rowsPerPage);

    // Prevent going beyond the first or last page
    if (pageNumber < 1) {
      pageNumber = 1;
    }
    if (pageNumber > totalPages) {
      pageNumber = totalPages;
    }

    // Calculate which rows should be visible for this page
    const start = (pageNumber - 1) * rowsPerPage; // Start index (inclusive)
    const end = start + rowsPerPage; // End index (exclusive)

    // Hide all rows first
    rows.forEach((row) => (row.style.display = 'none'));

    // Show only the rows that belong to the current page
    rows.slice(start, end).forEach((row) => (row.style.display = ''));

    // Update global current page and highlight the active pagination button
    currentPage = pageNumber;
    updateActivePage(pageNumber);
  }

  // Highlight the active pagination button
  function updateActivePage(activePage) {
    // Select all the pagination bar buttons/items
    const pageItems = paginationContainer.querySelectorAll('.page-item');

    // Remove the `active` class from all the buttons in the pagination bar
    pageItems.forEach((item) => item.classList.remove('active'));

    // Add the `active` class to the selected page button
    // (data-page = An attribute that each numbered element in the pagination bar is given and is equal to corresponding number)
    const activeButton = paginationContainer.querySelector(`[data-page="${activePage}"]`);
    if (activeButton) {
      activeButton.classList.add('active');
    }
  }

  // Generate pagination controls dynamically
  function setupPagination() {
    // Calculate how many total pages are needed
    const totalPages = Math.ceil(rows.length / rowsPerPage);

    // Clear any existing pagination
    paginationContainer.innerHTML = '';

    // === Create "Previous" Page Button ===
    // Create List-Element <li> in Pagination Bar (Which is actually an unordered list <ul>)
    const prevItem = document.createElement('li');

    // Give it the class `page-item`
    prevItem.classList.add('page-item');

    // Make the element work as a hyperlink
    prevItem.innerHTML = `
            <a class="page-link" href="#" aria-label="Previous">
                <span class="material-symbols-outlined">chevron_left</span>
            </a>
        `;

    // Move to previous page (if not already on page 1)
    prevItem.addEventListener('click', (e) => {
      // Prevents the default <a href="#"> from jumping to the top of the page
      // In other words, do no reload, and handle page switching purely with JavaScript
      e.preventDefault();

      if (currentPage > 1) {
        // Display the new page
        displayPage(currentPage - 1);
      }
    });

    // Append (add) the "Previous" button <li> element to the pagination <ul> container,
    // making the button visible on the webpage inside the pagination bar
    paginationContainer.appendChild(prevItem);

    // === Create "Numbered" Page Buttons ===
    for (let i = 1; i <= totalPages; i++) {
      // Create List-Element <li> in Pagination Bar (Which is actually an unordered list <ul>)
      const pageItem = document.createElement('li');

      // Give it the class `page-item`
      pageItem.classList.add('page-item');

      // Make the element work as a hyperlink
      pageItem.innerHTML = `<a class="page-link" href="#">${i}</a>`;

      // Give the pageItem the attribute data-page and set it equal to its page number.
      pageItem.setAttribute('data-page', i);

      // When the user clicks a page number, load that page
      pageItem.addEventListener('click', (e) => {
        // Prevents the default <a href="#"> from jumping to the top of the page
        // In other words, do no reload, and handle page switching purely with JavaScript
        e.preventDefault();

        // Display the new page
        displayPage(i);
      });

      // Append (add) the "Numbered" button <li> element to the pagination <ul> container,
      // making the button visible on the webpage inside the pagination bar
      paginationContainer.appendChild(pageItem);
    }

    // === Create "Next" Button ===
    // Create List-Element <li> in Pagination Bar (Which is actually an unordered list <ul>)
    const nextItem = document.createElement('li');

    // Give it the class `page-item`
    nextItem.classList.add('page-item');

    // Make the element work as a hyperlink
    nextItem.innerHTML = `
            <a class="page-link" href="#" aria-label="Next">
                <span class="material-symbols-outlined">chevron_right</span>
            </a>
        `;

    // Move to next page (if not already on the last one)
    nextItem.addEventListener('click', (e) => {
      // Prevents the default <a href="#"> from jumping to the top of the page
      // In other words, do no reload, and handle page switching purely with JavaScript
      e.preventDefault();

      if (currentPage < totalPages) {
        // Display the new page
        displayPage(currentPage + 1);
      }
    });

    // Append (add) the "Next" button <li> element to the pagination <ul> container,
    // making the button visible on the webpage inside the pagination bar
    paginationContainer.appendChild(nextItem);
  }

  // Initialize Pagination on Page Load
  // Generate pagination buttons based on total products
  setupPagination();

  // Always start by showing the first page
  displayPage(1);
});
