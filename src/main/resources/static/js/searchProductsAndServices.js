// Live search for tables (client-side filtering)

(function() {
  const DEBOUNCE_MS = 250;
  let timer = null;

  let params = new URLSearchParams(document.location.search);
  let table_filter = params.get("filter");

  document.addEventListener('DOMContentLoaded', () => {
    const input = document.getElementById('search-input');
    const product_tbody = document.getElementById('productTable-body');
    const service_tbody = document.getElementById('serviceTable-body');

    if (!input) return;

    const filterNow = () => {
      const q = (input.value || '').trim().toLowerCase();
      let rows;
      if (table_filter === "products") {
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
      } else if (table_filter === "services") {
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
  });
})();
