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
        // Collect job values using Thymeleaf attribute values on the buttn.
        const btn = e.currentTarget;

        const job = {
            id: btn.dataset.jobId || '',
            title: btn.dataset.jobTitle || '',
            customer_name: btn.dataset.jobCustomerName || '',
            customer_phone: btn.dataset.jobCustomerPhone || '',
            work_time_minutes: parseInt(btn.dataset.jobWorkTime) || 0,
            price_per_minute: parseFloat(btn.dataset.jobPrice) || 0,
            date: btn.dataset.jobDate || '',
            status: {id: parseInt(btn.dataset.jobStatusId) || 1},
        };
        // Call modal opener if available
        if (window.openEditJobModal) window.openEditJobModal(job);
    });

    // Open Description modal and pass current description text
    document.getElementById('openDescBtn')?.addEventListener('click', (e) => {
        e.preventDefault();
        const id = getRepairIdFromUrl()
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

    // Event listener to remove a product from a repair
    const removeBtns = document.querySelectorAll('.remove-btn');

    removeBtns.forEach(btn => {
        btn.addEventListener('click', (e) => {
            let productId = parseInt(e.target.getAttribute("data-id"));
            let repairId = getRepairIdFromUrl(); // Use a function "add-product-to-repair-modal.js" to get the repair id from the url.
            let type = e.target.getAttribute("data-type");

            let payload = [{
                repairId,
                productId,
                type,
            }];

            console.log(payload)
            console.log(JSON.stringify(payload))

            fetch("/api/repairs/removeProduct", {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(payload),
            }).then((r) => {
                console.log("trying to parse")
                if (!r.ok) throw new Error('Create failed'); // Handle HTTP errors
                return r.text();
            }).then((r) => {
                console.log("trying to reload")
                window.location.reload()
            })

        })
    })

    document.getElementById('deleteBtn').addEventListener('click', function () {
        const jobId = this.getAttribute('data-job-id');
        if (confirm('Slet reparation?')) {
            fetch('/api/jobs/' + jobId, {method: 'DELETE'})
                .then(resp => {
                    if (resp.ok) {
                        window.location.href = '/jobliste';
                    } else {
                        alert('Could not delete job');
                    }
                })
                .catch(() => alert('Could not connect to server'));
        }
    });

    // Eventlistener for the edit "arbejdstid" btn
    let editBtn = document.getElementById('edit-worktime-btn')
    editBtn.addEventListener('click', (e) => {
        // Get the current data from the job using Thymeleaf attributes
        const repairId = e.currentTarget.getAttribute('data-id')
        const workTime = e.currentTarget.getAttribute('data-workTimeMin')
        const workCost = e.currentTarget.getAttribute('data-workCost')

        // Get the table cells for the work time and work cost + the table row (for changing the border on the row when the user can edit)
        let workTimeTd = document.getElementById('workTimeTd')
        let workCostTd = document.getElementById('workCostTd')
        let tableRow = document.getElementById('worktime-table-row')

        // Change the worktime and work cost table cells to input boxes
        let workTimeInput = document.createElement('input')
        workTimeInput.type = "number"
        workTimeInput.className = "w-25 form-control form-control-sm"
        workTimeInput.value = workTime
        let workCostInput = document.createElement('input')
        workCostInput.type = "number"
        workCostInput.className = "w-30 form-control form-control-sm"
        workCostInput.value = workCost

        // Replace the current <p> tags with <input> tags inside the table cells
        workTimeTd.innerHTML = ''
        workTimeTd.appendChild(workTimeInput)
        workCostTd.innerHTML = ''
        workCostTd.appendChild(workCostInput)

        // Set focus in the workTime input
        workTimeInput.focus()

        // Change the border on the table row, so that the user knows where to edit
        tableRow.className = "border-left-0 border-2 border-primary"

        // Chang the apperance of the edit btn to "gem"
        editBtn.innerHTML = ''
        editBtn.innerText = "Gem"
        editBtn.className = "btn btn-sm btn-success align-co px-1 py-1 pt-0"

        // Cloning the object removes event listeners
        let editBtnClone = editBtn.cloneNode(true);

        // Add an eventListener to the edit btn, which updates the job with the values in the input fields and then reloads the page
        editBtnClone.addEventListener('click', async (e) => {
            // Define the payload with worktime and work cost
            const payload = {
                work_time_minutes: workTimeInput.value,
                price_per_minute: workCostInput.value
            }

            // Perform PUT request to update the job
            fetch('/api/jobs/' + repairId + "/update", {
                method: 'PUT',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(payload),
            })
                .then((r) => {
                    if (!r.ok) throw new Error('Update failed');
                    return r.json();
                })
                .then(() => {
                    window.location.reload(); // Refresh page to reflect changes
                })
                .catch((err) => {
                    console.error(err);
                    // User-facing error (Danish): "Could not update the job."
                    alert('Kunne ikke opdatere jobbet.');
                });
        })

        editBtn.parentNode.replaceChild(editBtnClone, editBtn); // Replacing the original btn with the cloned object with the


    })


});
