(function () {
    // Find the modal container; if it's not present, stop running (prevents errors on pages without the modal)
    const modalEl = document.getElementById('editJobModal');
    if (!modalEl) return;

    // Initialize the Bootstrap modal instance for programmatic control
    const editModal = new bootstrap.Modal(modalEl);

    // Function to convert date time to IsoLocal time
    function toIsoLocal(val) {
        if (!val) return null;
        return val.length === 16 ? val + ':00' : val;
    }

    // Submit handler for the edit form (if the form exists on the page)
    document.getElementById('editJobForm')?.addEventListener('submit', (e) => {
        e.preventDefault(); // Prevent a normal form submit / page navigation

        // Read the job ID to build the request URL
        const id = document.getElementById('jobId').value;

        // Build the payload from the form inputs. Use safe parsing and sensible defaults.
        const payload = {
            title: document.getElementById('title').value,
            customer_name: document.getElementById('customer_name').value,
            customer_phone: document.getElementById('customer_phone').value,
            duration: parseInt(
                document.getElementById('duration').value || '0',
                10,
            ),
            date: toIsoLocal(document.getElementById('date').value),
            status: {id: parseInt(document.getElementById('status_id').value, 10)},
        };

        // Perform PUT request to update the job
        fetch('/api/jobs/' + id + "/update", {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(payload),
        })
            .then((r) => {
                if (!r.ok) throw new Error('Update failed'); // Convert non-2xx to an error
                return r.json(); // Parse response JSON (if any)
            })
            .then(() => {
                editModal.hide(); // Close the modal on success
                window.location.reload(); // Refresh page to reflect changes (simple, reliable)
            })
            .catch((err) => {
                console.error(err);
                // User-facing error (Danish): "Could not update the job."
                alert('Kunne ikke opdatere jobbet.');
            });
    });

    // Open the model in the current window and also prefill inout field with the existing values in the job
    window.openEditJobModal = function (job) {
        // Populate hidden job ID
        document.getElementById('jobId').value = job.id;

        // Populate simple text fields (use empty string fallback)
        document.getElementById('title').value = job.title || '';
        document.getElementById('customer_name').value = job.customer_name || '';
        document.getElementById('customer_phone').value = job.customer_phone || '';

        // Numeric fields: allow blank when undefined/null using nullish coalescing
        document.getElementById('duration').value = job.duration ?? '';

        // Date handling:
        // - If job.date exists and is already in "YYYY-MM-DDTHH:MM" (length 16), use it directly.
        // - If job.date includes seconds (longer than 16), slice to first 16 characters to fit datetime-local input.
        // - Otherwise set to empty string.
        document.getElementById('date').value = job.date
            ? job.date.length === 16
                ? job.date
                : job.date.slice(0, 16)
            : '';

        // Status: prefer nested status.id, else fallback to status_id, else default to 1
        document.getElementById('status_id').value = job.status?.id || job.status_id || 1;

        // Show the modal after fields are populated
        editModal.show();
    };
})();
