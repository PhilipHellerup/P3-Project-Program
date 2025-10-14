(function () {
  // Locate the description modal by ID
  const modalEl = document.getElementById('descModal');
  if (!modalEl) return; // Exit if modal not found (avoids errors on pages without it)

  // Initialize Bootstrap modal instance
  const descModal = new bootstrap.Modal(modalEl);

  // Handle submission of the "Edit Description" form
  document.getElementById('editDescForm')?.addEventListener('submit', (e) => {
    e.preventDefault(); // Prevent default form submission

    // Get job ID and description input values
    const id = document.getElementById('jobIdDesc').value;
    const payload = {
      job_description: document.getElementById('job_description_desc').value,
    };

    // Send PUT request to update job description
    fetch('/api/jobs/' + id + '/description', {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })
      .then((r) => {
        if (!r.ok) throw new Error('Update description failed'); // Handle server errors
        // Try to parse JSON response safely (optional chaining fallback)
        return r.json?.() ?? {};
      })
      .then(() => {
        descModal.hide(); // Close modal after successful update

        // Update the visible job description text on the page (if present)
        const p = document.getElementById('jobDescriptionText');
        if (p) p.textContent = payload.job_description;
      })
      .catch((err) => {
        console.error(err);
        alert('Kunne ikke opdatere beskrivelsen.'); // User-facing error message (Danish: “Could not update the description.”)
      });
  });

  /**
   * Expose a global helper to open the modal and prefill form fields.
   * Used when editing an existing job description.
   * @param {number|string} jobId - The job’s unique ID
   * @param {string} currentText - The current job description
   */
  window.openDescriptionModal = function (jobId, currentText) {
    const ta = document.getElementById('job_description_desc');
    const idEl = document.getElementById('jobIdDesc');

    // Pre-fill the form with the current job data
    if (idEl) idEl.value = jobId;
    if (ta) ta.value = currentText || '';

    // Display the modal
    descModal.show();
  };
})();
