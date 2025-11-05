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

    // todo: this will be replaced with the search logic
    // Add an eventListener to the search bar
    document.getElementById('searchBar').addEventListener('click', async (e) => {
        e.preventDefault()


        let searchParams = document.getElementById('searchBar').value;
        let matches = await fetchSearchMatches(searchParams)

        matches.forEach(match => {
            addNewProductToTable(match.name, 1, match.price, match.price)
        })

    })

    // Add eventListener to the "add product" btn, which sends a HTTP request to the JobController API endpoint, for a product to be added to a specific job.
    document.getElementById('confirm-add-product-btn').addEventListener('click', async (e) => {
        e.preventDefault()

        // Define the payload


        // Create a request to send the paylond to the jobController
        fetch('/api/repairs/addProduct', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({repairID: 8, productId: 1, quantity: 2})
        })
            .then((r) => {
                if (!r.ok) throw new Error("Failed to add product to repair")
            }).then(() => {
            createModal.hide() // Hide the modal, when the repair has been added successfully
            window.location.reload();
        })
    })
})();

function addNewProductToTable(name, amount, partPrice, totalPrice) {
    let tablebody = document.getElementById('table-body');
    let newProduct = document.createElement('tr');
    newProduct.innerHTML = `
                  <td class="w-15">
                    <p>${name}</p>
                  </td>
                  <td class="w-15">
                    <p>${amount}</p>
                  </td>
                  <td class="w-30">
                    <p>${partPrice}</p>
                  </td>
                  <td class="w-30">
                     <p>${totalPrice}</p>
                  </td>
                `;

    // Append to the table-body
    tablebody.appendChild(newProduct);
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
