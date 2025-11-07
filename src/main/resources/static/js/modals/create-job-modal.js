// Store services added within the modal to an array.
let modalServices = [];

// Get all the elements we need from the DOM
let searchBar = document.getElementById('serviceSearchBar');
let createRepairForm = document.getElementById('createJobForm');
let searchResults = document.getElementById('service-search-results');
let searchTable = document.getElementById('service-search-table');
let totalPrice = document.getElementById('total-price-create-repair');
let totalDuration = document.getElementById('totalDuration');
let workTimeInput = document.getElementById('workTime');
let workCostPrMin = document.getElementById('workCost');
let totalWorkCost = document.getElementById('workTotalCost');

(function () {
    // Locate the modal element by ID
    const modalEl = document.getElementById('createJobModal');
    if (!modalEl) return; // Exit if modal not found (prevents errors on pages without it)

    // Initialize Bootstrap modal instance
    const createModal = new bootstrap.Modal(modalEl);

    /**
     * Convert a datetime-local input value to a full ISO 8601 string.
     * Ensures seconds are included (adds ":00" if needed).
     * @param {string} val - The datetime-local input value
     * @returns {string|null} ISO-compatible datetime string or null
     */
    function toIsoLocal(val) {
        if (!val) return null;
        return val.length === 16 ? val + ':00' : val;
    }

    // Show search results, when clicking the search bar which already has a value
    searchBar.addEventListener('click', (e) => {
        if (e.target.value) {
            showSearchResults(searchTable)
        }
    })

    // Add an evenetListener to the search bar
    searchBar.addEventListener('input', async (e) => {
        e.preventDefault();

        // Get search results using the search Controller API
        let searchParams = searchBar.value;
        let matches = await fetchSearchMatches(searchParams)

        searchResults.innerHTML = ''
        if (!matches) {
            searchTable.style.display = "none"
        } else {
            matches.forEach(match => {
                let newResult = document.createElement('tr')
                newResult.addEventListener('click', (e) => {
                    // The if the product is already on the list. If it is, increate the quantity by one. If not, add the products to the list.
                    const existing = modalServices.find(p =>
                        String(p.product.id) === String(match.id));

                    if (existing) {
                        existing.quantity += 1;
                    } else {
                        modalServices.push({product: match, quantity: 1, productType: "service"});
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
                        </div>
                    <div class="d-flex justify-content-between gap-5">
                        <div>
                           <p class="mb-0 fs-7">${match.duration} min.</p> <!-- EAN has to be lowercase (ean), beucase the JSON ojbect "match" from the HTTP repose, are lowercase -->
                        </div>
                           <div>
                           <p class="mb-0 fs-7">${match.price} kr.</p>
                        </div>
                     </div>
                    </td>
                `
                searchResults.appendChild(newResult)
                showSearchResults(searchTable)
            })
        }

    })

    // Eventlistener on the workTime and WorkCost inputs to change totalWorkCost on inputchange
    workTimeInput.addEventListener('input', handleWorkInputChange)
    workCostPrMin.addEventListener('input', handleWorkInputChange)

    // Attach submit listener to the form (if it exists)
    createRepairForm.addEventListener('submit', function (e) {
        e.preventDefault(); // Prevent page reload

        // Format date and time as DateTime
        const dateValue = document.getElementById('jobDate').value;
        const timeValue = document.getElementById('jobTime').value;

        const isoString = `${dateValue}T${timeValue}:00`; // "2025-10-28T13:45:00"

        // Collect and sanitize form data into a payload object
        const newRepair = {
            title: document.getElementById('jobTitle').value,
            customer_name: document.getElementById('customerName').value,
            customer_phone: document.getElementById('customerPhone').value,
            job_description: document.getElementById('jobDescription').value,
            work_time_minutes: parseInt(document.getElementById('workTime').value || '0', 10),
            price_per_minute: parseFloat(document.getElementById('workCost').value || '0'),
            duration: parseFloat(document.getElementById('totalDuration').value || '0'),
            date: isoString,
            status: {id: parseInt(document.getElementById('jobStatus').value, 10)},
        };

        // Map services into the array. We only need 'id' and 'quantity' for adding them to the repair
        const servicesArray = modalServices.map(item => ({
            id: item.product.id,
            quantity: item.quantity
        }));

        // Spread the newRepair fields and the services
        const payload = {
            ...newRepair,
            services: servicesArray
        };

        // Debugging
        console.log(payload);

        // Send POST request to create a new job entry
        fetch('/api/jobs/create', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(payload),
        })
            .then((r) => {
                if (!r.ok) throw new Error('Create failed'); // Handle HTTP errors
                return r.json();
            })
            .then((created) => {
                createModal.hide(); // Close the modal on success

                // Refresh calendar view if function available, else reload page
                if (
                    window.refreshCalendarWithJob &&
                    typeof window.refreshCalendarWithJob === 'function'
                ) {
                    window.refreshCalendarWithJob(created);
                } else {
                    window.location.reload();
                }
            })
            .catch((err) => {
                console.error(err);
                alert('Failed to create job'); // Notify user of failure
            });
    });

    /**
     * Expose a global helper to open the modal and reset the form.
     * Can be called from buttons or other scripts.
     */
    window.openCreateJobModal = function () {
        document.getElementById('createJobForm')?.reset();
        createModal.show();
    };
})();

// Search in product using the productController API endpoint
async function fetchSearchMatches(searchParam) {
    try {
        // Send PUT request to update the job entry
        const r1 = await fetch('/api/search/service?q=' + searchParam, {
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
        !searchResults.contains(event.target) && !searchBar.contains(event.target);

    if (clickedOutside) {
        hideSearchResults(searchTable);
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

function renderProductTable() {
    const tableBody = document.getElementById('service-added-table-body');
    tableBody.innerHTML = ''; // clear current rows

    modalServices.forEach((item) => {
        const row = document.createElement('tr');
        // Set the productid of the row, so that the remove buttons knows which product to remove from modalServices
        row.dataset.productId = item.product.id
        row.style.marginBottom = '0'
        row.innerHTML = `
        <td class="w-15">
            <p class="mt-0">${item.product.name}</p>
        </td>
        <td class="w-15">
            <input type="number" class="form-control form-control-sm quantity-input" 
                       value="${item.quantity}" min="1" style="width: 3rem;">
        </td>
        <td class="w-30">
            <p class="mt-0">${item.product.duration} min</p>
        </td>
        <td class="w-30">
            <p class="mt-0">${item.product.price} kr.</p>
        </td>
        <td class="w-30">
            <p class="mt-0">${item.product.price * item.quantity} kr.</p>
        </td>
        <td class="w-10 text-center">
            <button type="button" class="btn btn-sm btn-danger remove-btn">X</button>
        </td>
    `;

        // Listen for quantity change in the input field for each row. When the quantity is changed, also change the quantity attribute in the modalServices array
        const qtyInput = row.querySelector('.quantity-input');
        qtyInput.addEventListener('input', (e) => {
            const newQty = parseInt(e.target.value, 10);

            const id = parseInt(row.dataset.productId, 10);
            const productItem = modalServices.find(p => p.product.id === id);
            if (productItem) {
                productItem.quantity = newQty;
            }

            // Update price and duration, if the input is not blank.
            if (newQty) {
                updatePriceAndDuration();
            }

        });

        row.querySelector('.remove-btn').addEventListener('click', () => {
            const id = parseInt(row.dataset.productId, 10);
            modalServices = modalServices.filter(p => p.product.id !== id);
            renderProductTable(modalServices)
        });

        tableBody.appendChild(row);

        // Update price and duration
        updatePriceAndDuration();
    });


}

function handleWorkInputChange() {
    totalWorkCost.innerText = (Number(workTimeInput.value) * Number(workCostPrMin.value)).toFixed(2);
    updatePriceAndDuration()
}

function updatePriceAndDuration() {
    // Set the total cost equal to the cost of products.
    // Calculate and set to total duration of the job. This duration can later be editid by the user
    let duration = 0;
    let total = 0;
    modalServices.forEach(service => {
        total += service.product.price * service.quantity;
        duration += service.product.duration * service.quantity
    });
    total += parseInt(totalWorkCost.innerText)
    totalPrice.innerText = total;
    totalDuration.value = duration;

}

// Function to add the services chosen here to the products.
function addServicesToRepair() {
    // Define the payload to send
    const payload = modalServices.map(item => ({
        repairId: "8", // Placeholder. Find a way to get the id of the new created repair
        productId: item.product.id,
        quantity: item.quantity,
        type: item.productType
    }))

    fetch("api/repairs/addProduct", {
        method: post,
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(payload)
    }).then((r) => {
        if (!r.ok) {
            throw new Error("failed to add product to repair")
        }
    })
}