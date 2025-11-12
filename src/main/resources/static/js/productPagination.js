// paginationProductService.js
document.addEventListener('DOMContentLoaded', () => {
  const rowsPerPage = 7;
  const tableBody = document.querySelector('table tbody');
  const paginationContainer = document.querySelector('.pagination');
  let currentPage = 1;

  function getVisibleRows() {
    // Only include rows that are not hidden (by search)
    return Array.from(tableBody.querySelectorAll('tr:not(.d-none)'));
  }

  function displayPage(pageNumber) {
    const rows = getVisibleRows();
    const totalPages = Math.max(1, Math.ceil(rows.length / rowsPerPage));

    // Clamp page number
    pageNumber = Math.min(Math.max(pageNumber, 1), totalPages);

    // Hide all rows
    Array.from(tableBody.querySelectorAll('tr')).forEach((row) => (row.style.display = 'none'));

    // Show rows for the current page
    const start = (pageNumber - 1) * rowsPerPage;
    const end = start + rowsPerPage;
    rows.slice(start, end).forEach((row) => (row.style.display = ''));

    currentPage = pageNumber;
    updateActivePage(pageNumber);
  }

  function updateActivePage(activePage) {
    paginationContainer.querySelectorAll('.page-item').forEach((item) => item.classList.remove('active'));
    const activeButton = paginationContainer.querySelector(`[data-page="${activePage}"]`);
    if (activeButton) activeButton.classList.add('active');
  }

  function setupPagination() {
    const rows = getVisibleRows();
    const totalPages = Math.max(1, Math.ceil(rows.length / rowsPerPage));
    paginationContainer.innerHTML = '';

    // === Previous button ===
    const prevItem = document.createElement('li');
    prevItem.classList.add('page-item');
    prevItem.innerHTML = `<a class="page-link" href="#"><span class="material-symbols-outlined">chevron_left</span></a>`;
    prevItem.addEventListener('click', (e) => {
      e.preventDefault();
      if (currentPage > 1) displayPage(currentPage - 1);
    });
    paginationContainer.appendChild(prevItem);

    // === Numbered buttons ===
    for (let i = 1; i <= totalPages; i++) {
      const pageItem = document.createElement('li');
      pageItem.classList.add('page-item');
      pageItem.innerHTML = `<a class="page-link" href="#">${i}</a>`;
      pageItem.setAttribute('data-page', i);
      pageItem.addEventListener('click', (e) => {
        e.preventDefault();
        displayPage(i);
      });
      paginationContainer.appendChild(pageItem);
    }

    // === Next button ===
    const nextItem = document.createElement('li');
    nextItem.classList.add('page-item');
    nextItem.innerHTML = `<a class="page-link" href="#"><span class="material-symbols-outlined">chevron_right</span></a>`;
    nextItem.addEventListener('click', (e) => {
      e.preventDefault();
      if (currentPage < totalPages) displayPage(currentPage + 1);
    });
    paginationContainer.appendChild(nextItem);
  }

  // Public API for other scripts
  window.Pagination = {
    refresh: () => {
      setupPagination();
      displayPage(1); // Always reset to first page after search
    }
  };

  // Initial setup
  setupPagination();
  displayPage(1);
});
