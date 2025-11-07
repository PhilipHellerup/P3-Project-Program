// A list of products to be added by the current modal. When the modal is opened again, the list of products are reset.
let modalProducts = [];

(function () {
    const modalEl = document.getElementById('addProductToRepair');
    if (!modalEl) {
        console.warn("Modal element #addProductToRepair not found");
        return;
    }
    const createModal = new bootstrap.Modal(modalEl);

    window.openAddProductToRepairModal = function () {
        const form = document.getElementById('addProductToRepairForm');

        // Reset the form (if it exists)
        if (form) form.reset();

        // Show the modal
        createModal.show();
    };

    // Stop form submit with "enter" keypress
    document.getElementById('addProductToRepairForm').addEventListener('keydown', (e) => {
        if (e.key === 'Enter') {
            e.preventDefault();
        }
    });

    // Add an eventListener to the search bar
    let searchTable = document.getElementById('search-table')
    let searchResults = document.getElementById('search-results')
    let searchBar = document.getElementById('searchBar')
    searchBar.addEventListener('input', async (e) => {
        e.preventDefault()

        // Get search results using the search Controller API
        let searchParams = document.getElementById('searchBar').value;
        let matches = await fetchSearchMatches(searchParams)

        // Show search results as a dropdown below the search bar
        searchResults.innerHTML = ''
        if (!matches) {
            searchTable.style.display = "none"
        } else {
            matches.forEach(match => {
                let newResult = document.createElement('tr')
                newResult.addEventListener('click', (e) => {
                    // The if the product is already on the list. If it is, increate the quantity by one. If not, add the products to the list.
                    const existing = modalProducts.find(p => p.product.id === match.id);

                    if (existing) {
                        existing.quantity += 1;
                    } else {
                        modalProducts.push({product: match, quantity: 1});
                    }
                    // Add the product to modal UI and update the UI
                    renderProductTable()
                    searchBar.value = '';
                    searchBar.select();
                    hideSearchResults(searchTable)
                })
                newResult.innerHTML = `
                    <td class="d-flex justify-content-between">
                        <div>
                           <p class="fs-6 mb-0">${match.name}</p>
                           <p class="text-secondary mb-0 fs-7">${match.type}</p>
                        </div>
                    <div class="d-flex justify-content-between gap-5">
                        <div>
                           <p class="fs-6 mb-0">EAN</p>
                           <p class="text-secondary mb-0 fs-7">${match.ean}</p> <!-- EAN has to be lowercase (ean), beucase the JSON ojbect "match" from the HTTP repose, are lowercase -->
                        </div>
                           <div>
                           <p class="fs-6 mb-0">Pris</p>
                           <p class="text-secondary mb-0 fs-7">${match.price}</p>
                        </div>
                     </div>
                    </td>
                `
                searchResults.appendChild(newResult)
                showSearchResults(searchTable)
            })
        }
    })


    // Show search results, when clicking the search bar which already has a value
    searchBar.addEventListener('click', (e) => {
        if (e.target.value) {
            showSearchResults(searchTable)
        }
    })

    // Add eventListener to the "add product" btn, which sends a HTTP request to the JobController API endpoint, for a product to be added to a specific job.
    document.getElementById('confirm-add-product-btn').addEventListener('click', async (e) => {
        e.preventDefault()

        // Define the payload from the products in modalProducts and get their id. Also get the repair id. Quantity is set to 1, but should be changed to match the modal
        // Get the repairId from the url
        const repairId = getRepairIdFromUrl();
        const payload = modalProducts.map(item => ({
            repairId: repairId,
            productId: item.product.id,
            quantity: item.quantity
        }))

        console.log(JSON.stringify(payload))

        // Create a request to send the paylond to the jobController
        fetch('/api/repairs/addProduct', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(payload)
        })
            .then((r) => {
                if (!r.ok) throw new Error("Failed to add product to repair")
            }).then(() => {
            createModal.hide() // Hide the modal, when the repair has been added successfully
            window.location.reload();
        })
    })
})();

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
            <p class="mt-0">${item.product.price}</p>
        </td>
        <td class="w-30">
            <p class="mt-0">${item.product.price}</p>
        </td>
        <td class="w-10 text-center">
            <button type="button" class="btn btn-sm btn-danger remove-btn">X</button>
        </td>
    `;

        // Listen for quantity change in the input field for each row. When the quantity is changed, also change the quantity attribute in the modalProducts array
        const qtyInput = row.querySelector('.quantity-input');
        qtyInput.addEventListener('input', (e) => {
            const newQty = parseInt(e.target.value, 10);

            const id = parseInt(row.dataset.productId, 10);
            const productItem = modalProducts.find(p => p.product.id === id);
            if (productItem) {
                productItem.quantity = newQty;
            }
        });

        row.querySelector('.remove-btn').addEventListener('click', () => {
            const id = parseInt(row.dataset.productId, 10);
            modalProducts = modalProducts.filter(p => p.id !== id);
            renderProductTable(modalProducts)
        });

        tableBody.appendChild(row);
    });
}


// Seach in product using the productController API endpoint
async function fetchSearchMatches(searchParam) {
    try {
        // Send PUT request to update the job entry
        const r1 = await fetch('/api/search/repair?q=' + searchParam, {
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

// logic to hide the seach results, when the user clicks outside of the search bar
// The listener that checks if user clicked outside
function handleClickOutside(event) {
    const clickedOutside =
        !document.getElementById('search-results').contains(event.target) && !document.getElementById('searchBar').contains(event.target);

    if (clickedOutside) {
        hideSearchResults(document.getElementById('search-table'));
    }
}

// Function to hide the table and remove click-outside listener
function hideSearchResults(table) {
    table.style.display = 'none';
    document.removeEventListener('click', handleClickOutside);
}

// Function to show the table and start click-outside listener
function showSearchResults(table) {
    table.style.display = 'table';

    // small delay to prevent immediately closing it from the same click
    setTimeout(() => {
        document.addEventListener('click', handleClickOutside);
    }, 0);
}

// function to get repair id from URL
function getRepairIdFromUrl() {
    const parts = window.location.pathname.split('/');
    return parseInt(parts[parts.length - 1], 10);
}

/// Depreciated
/*
function addNewProductToTable(name, amount, productPrice) {
    const tableBody = document.getElementById('table-body');
    const newProduct = document.createElement('tr');

    newProduct.innerHTML = `
        <td class="w-15">
            <p class="mt-0">${name}</p>
        </td>
        <td class="w-15">
            <p class="mt-0">${amount}</p>
        </td>
        <td class="w-30">
            <p class="mt-0">${productPrice}</p>
        </td>
        <td class="w-30">
            <p class="mt-0">${productPrice}</p>
        </td>
        <td class="w-10 text-center">
            <button type="button" class="btn btn-sm btn-danger remove-btn">X</button>
        </td>
    `;

    // Add event listener to the button
    newProduct.querySelector('.remove-btn').addEventListener('click', () => {
        newProduct.remove();
    });

    // Append to the table body
    tableBody.appendChild(newProduct);
}
*/


