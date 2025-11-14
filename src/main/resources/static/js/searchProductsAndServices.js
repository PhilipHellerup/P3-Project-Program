// Live search for tables (client-side filtering)

(function() {
  const DEBOUNCE_MS = 250;
  let timer = null;

  document.addEventListener('DOMContentLoaded', () => {
    const input = document.getElementById('search-input');
    const product_tbody = document.getElementById('productTable-body');
    const service_tbody = document.getElementById('serviceTable-body');

    if (!input) return;

    if (product_tbody) {
      input.placeholder = "Søg efter reservedele";
    } else if (service_tbody) {
      input.placeholder = "Søg efter ydelser";
    }

    const filterNow = () => {
      const q = (input.value || '').trim().toLowerCase();
      let rows;
      if (product_tbody) {
        rows = product_tbody.querySelectorAll('tr');
        if (q === "") {
          rows.forEach((row) => row.classList.remove('d-none'));
          if (window.Pagination) window.Pagination.refresh();
          return;
        }

        rows.forEach((row) => {
          const pn = row.querySelector('td[data-field="productNumber"]')?.textContent?.toLowerCase() || '';
          const name = row.querySelector('td[data-field="name"]')?.textContent?.toLowerCase() || '';
          const ean = row.querySelector('td[data-field="EAN"]')?.textContent?.toLowerCase() || '';
          const type = row.querySelector('td[data-field="type"]')?.textContent?.toLowerCase() || '';
          const matches = pn.includes(q) || name.includes(q) || ean.includes(q) || type.includes(q);
          row.classList.toggle('d-none', !matches);
        });
        if (window.Pagination) {
          window.Pagination.refresh();
        }
      } else if (service_tbody) {
        rows = service_tbody.querySelectorAll('tr');
        if (q === "") {
          rows.forEach((row) => row.classList.remove('d-none'));
          if (window.Pagination) window.Pagination.refresh();
          return;
        }

        rows.forEach((row) => {
          const sn = row.querySelector('td[data-field="id"]')?.textContent?.toLowerCase() || '';
          const name = row.querySelector('td[data-field="name"]')?.textContent?.toLowerCase() || '';
          const price = row.querySelector('td[data-field="price"]')?.textContent?.toLowerCase() || '';
          const dura = row.querySelector('td[data-field="duration"]')?.textContent?.toLowerCase() || '';
          const matches = sn.includes(q) || name.includes(q) || price.includes(q) || dura.includes(q);
          row.classList.toggle('d-none', !matches);
        });
        if (window.Pagination) {
          window.Pagination.refresh();
        }
      }
    }
    const debouncedFilter = () => {
      clearTimeout(timer);
      timer = setTimeout(filterNow, DEBOUNCE_MS);
    };

    input.addEventListener('input', debouncedFilter);

    const searchForm = input.closest('form');
    if (searchForm) {
      searchForm.addEventListener('submit', (e) => {
          e.preventDefault();       // stop page reload
      });
    }
  });
})();
