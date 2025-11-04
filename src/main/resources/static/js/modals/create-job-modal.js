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

    // Attach submit listener to the form (if it exists)
    document.getElementById('createJobForm')?.addEventListener('submit', function (e) {
        e.preventDefault(); // Prevent page reload

        // Format date and time as DateTime
        const dateValue = document.getElementById('jobDate').value;
        const timeValue = document.getElementById('jobTime').value;

        const isoString = `${dateValue}T${timeValue}:00`; // "2025-10-28T13:45:00"

        // Collect and sanitize form data into a payload object
        const payload = {
            title: document.getElementById('jobTitle').value,
            customer_name: document.getElementById('customerName').value,
            customer_phone: document.getElementById('customerPhone').value,
            job_description: document.getElementById('jobDescription').value,
            work_time_minutes: parseInt(document.getElementById('workTime').value || '0', 10),
            price_per_minute: parseFloat(document.getElementById('pricePerMin').value || '0'),
            duration: parseFloat(document.getElementById('jobDuration').value || '0'),
            date: isoString,
            status: { id: parseInt(document.getElementById('jobStatus').value, 10) },
        };

        console.log(payload);

        // Send POST request to create a new job entry
        fetch('/api/jobs', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload),
        })
            .then((r) => {
                if (!r.ok) throw new Error('Create failed'); // Handle HTTP errors
                return r.json();
            })
            .then((created) => {
                createModal.hide(); // Close the modal on success

                // Refresh calendar view if function available, else reload page
                if (window.refreshCalendarWithJob && typeof window.refreshCalendarWithJob === 'function') {
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
