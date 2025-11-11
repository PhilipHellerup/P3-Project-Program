(function () {
    // Locate the full edit modal element
    const modalEl = document.getElementById('fullEditJobModal');
    if (!modalEl) return; // Exit if modal not found (avoids runtime errors)

    // Initialize Bootstrap modal instance for programmatic control
    const fullModal = new bootstrap.Modal(modalEl);

    /**
     * Convert datetime-local input values to full ISO-like strings.
     * Adds seconds (":00") if missing, ensuring consistent formatting.
     * @param {string|null|undefined} val
     * @returns {string|null}
     */
    function toIsoLocal(val) {
        if (!val) return null;
        return val.length === 16 ? val + ':00' : val;
    }

    // Attach submit event listener to the full edit form
    document.getElementById('fullEditJobForm')?.addEventListener('submit', async (e) => {
        e.preventDefault(); // Prevent form from triggering a full page reload

        // Gather form field values into a payload object
        const id = document.getElementById('full_jobId').value;
        const Payload = {
            title: document.getElementById('full_title').value,
            customer_name: document.getElementById('full_customer_name').value,
            customer_phone: document.getElementById('full_customer_phone').value,
            job_description: document.getElementById('full_job_description').value || '',
            work_time_minutes: parseInt(document.getElementById('full_work_time_minutes').value || '0', 10),
            price_per_minute: parseFloat(document.getElementById('full_price_per_minute').value || '0'),
            date: toIsoLocal(document.getElementById('full_date').value),
            status: { id: parseInt(document.getElementById('full_status_id').value, 10) },
        };

        try {
            // Send PUT request to update the job entry
            const r1 = await fetch('/api/jobs/' + id, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(Payload),
            });

            if (!r1.ok) throw new Error('Kunne ikke opdatere jobbet'); // "Could not update the job"

            // Close modal and reload page after successful update
            fullModal.hide();
            window.location.reload();
        } catch (err) {
            console.error(err);
            // Show a user-friendly alert message on failure
            alert(err.message || 'Opdatering fejlede.'); // “Update failed.”
        }
    });

    /**
     * Expose a global helper function to open and prefill the full edit modal.
     * @param {Object} job - The job data used to populate the form fields.
     */
    window.openFullEditJobModal = function (job) {
        // Populate hidden and input fields with job data
        document.getElementById('full_jobId').value = job.id;
        document.getElementById('full_title').value = job.title || '';
        document.getElementById('full_customer_name').value = job.customer_name || '';
        document.getElementById('full_customer_phone').value = job.customer_phone || '';
        document.getElementById('full_work_time_minutes').value = job.work_time_minutes ?? '';
        document.getElementById('full_price_per_minute').value = job.price_per_minute ?? '';

        // Convert job date to proper datetime-local format
        document.getElementById('full_date').value = job.date
            ? job.date.length === 16
                ? job.date
                : job.date.slice(0, 16)
            : '';

        // Set job status using nested or fallback ID
        document.getElementById('full_status_id').value = job.status?.id || job.status_id || 1;

        // Fill in the description if available
        document.getElementById('full_job_description').value = job.job_description || '';

        // Display the modal after fields are populated
        fullModal.show();
    };
})();
