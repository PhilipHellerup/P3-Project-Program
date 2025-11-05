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
