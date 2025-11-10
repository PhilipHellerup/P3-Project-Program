document.addEventListener('DOMContentLoaded', () => {
    const el = document.getElementById('job-status');
    if (el) {
        const raw = el.textContent?.trim() ?? '';

        // Format the label if a translation function is available on window
        const label =
            typeof window.translateStatusName === 'function'
                ? window.translateStatusName(raw)
                : raw;

        // Get colors mapping (border/background/text) if provided by the app
        const colors =
            typeof window.statusToColors === 'function'
                ? window.statusToColors(raw)
                : {};

        // Apply text and inline styles to make the status look like a badge
        el.textContent = label;
        Object.assign(el.style, {
            display: 'inline-block',
            padding: '2px 8px',
            borderRadius: '3px',
            border: `1px solid ${colors.borderColor || '#ccc'}`,
            backgroundColor: colors.backgroundColor || '#eee',
            color: colors.textColor || '#000',
            fontWeight: '600',
        });

        // If the color helper provided a CSS class name, add it as well
        if (colors.cssClass) el.classList.add(colors.cssClass);
    }

    // Open Edit modal with current job prefilled via the module
    document.getElementById('openEditBtn')?.addEventListener('click', (e) => {
        e.preventDefault();
        // Collect job values using Thymeleaf inlined expressions (note: these become raw values on the client)
        const job = {
            id: '[[${job.id}]]' || '',
            title: '[[${job.title}]]',
            customer_name: '[[${job.customer_name}]]',
            customer_phone: '[[${job.customer_phone}]]',
            work_time_minutes: '[[${job.work_time_minutes}]]' || 0,
            price_per_minute: '[[${job.price_per_minute}]]' || 0,
            date: "[[${#temporals.format(job.date, 'yyyy-MM-dd''T''HH:mm')}]]" || '',
            status: {id: '[[${job.status.id}]]' || 1},
        };
        // Call modal opener if available
        if (window.openEditJobModal) window.openEditJobModal(job);
    });

    // Open Description modal and pass current description text
    document.getElementById('openDescBtn')?.addEventListener('click', (e) => {
        e.preventDefault();
        const id = '[[${job.id}]]' || '';
        const current =
            document.getElementById('jobDescriptionText')?.textContent ?? '';
        if (window.openDescriptionModal)
            window.openDescriptionModal(id, current.trim());
    });

    // Event listener for the "tilføj product" btn
    // todo: show to the user that the product was added
    // todo: do some data validation and error handling
    document.getElementById('add-product-btn').addEventListener('click', async (e) => {
        window.openAddProductToRepairModal()
    })
});


// Function to dynamically render a list of products on the page
function renderProductTable() {
    const tableBody = document.getElementById('table-body');
    tableBody.innerHTML = ''; // clear current rows

    modalProducts.forEach((item) => {
        const row = document.createElement('tr');
        // Set the productid of the row, so that the remove buttons knows which product to remove from modalProducts
        row.dataset.productId = item.product.id
        row.innerHTML = `
        <td class="w-15">
            <p class="mt-0">${item.product.name}</p>
        </td>
        <td class="w-15">
            <input type="number" class="form-control form-control-sm quantity-input" 
                       value="${item.quantity}" min="1" style="width: 3rem;">
        </td>
        <td class="w-30">
            <p class="mt-0">${item.product.price} kr.</p>
        </td>
        <td class="w-30">
            <p class="total-cost-field mt-0">${item.product.price * item.quantity} kr.</p>
        </td>
        <td class="w-10 text-center">
            <button type="button" class="btn btn-sm btn-danger remove-btn">X</button>
        </td>
    `;

        // Listen for quantity change in the input field for each row. When the quantity is changed, also change the quantity attribute in the modalProducts array
        const qtyInput = row.querySelector('.quantity-input');
        qtyInput.addEventListener('change', (e) => {
            const newQty = parseInt(e.target.value, 10);

            const id = parseInt(row.dataset.productId, 10);
            const productItem = modalProducts.find(p => p.product.id === id);
            if (productItem) {
                productItem.quantity = newQty;
                renderProductTable() // Also rerender the product table, to show the correct
            }
        });

        row.querySelector('.remove-btn').addEventListener('click', () => {
            const id = parseInt(row.dataset.productId, 10);
            modalProducts = modalProducts.filter(p => p.product.id !== id);
            renderProductTable(modalProducts)
        });

        tableBody.appendChild(row);
    });
}


// Search in jobParts and jobServices using the searchController API endpoint
async function fetchSearchMatches(searchParam) {
    try {
        // Send PUT request to update the job entry
        const r1 = await fetch('/api/search/products?q=' + searchParam, {
            method: 'GET',
            headers: {'Content-Type': 'application/json'},
        });

        // Get the matches from the response and update the page to show matches
        return await r1.json();
    } catch (err) {
        // todo: add some error handleing
        console.log(err);
    }
}
