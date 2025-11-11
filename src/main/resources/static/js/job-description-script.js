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

    document.getElementById('deleteBtn').addEventListener('click', function() {
        const jobId = this.getAttribute('data-job-id');
        if (confirm('Slet reparation?')) {
            fetch('/api/jobs/' + jobId, { method: 'DELETE' })
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
});