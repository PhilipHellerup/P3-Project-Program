(function () {
  const modalEl = document.getElementById('createJobModal');
  if (!modalEl) return;

  const createModal = new bootstrap.Modal(modalEl);

  function toIsoLocal(val) {
    if (!val) return null;
    return val.length === 16 ? val + ':00' : val;
  }

  document.getElementById('createJobForm')?.addEventListener('submit', function (e) {
    e.preventDefault();
    const payload = {
      title: document.getElementById('jobTitle').value,
      customer_name: document.getElementById('customerName').value,
      customer_phone: document.getElementById('customerPhone').value,
      job_description: document.getElementById('jobDescription').value,
      work_time_minutes: parseInt(document.getElementById('workTime').value || '0', 10),
      price_per_minute: parseFloat(document.getElementById('pricePerMin').value || '0'),
      date: toIsoLocal(document.getElementById('jobDate').value),
      status: { id: parseInt(document.getElementById('jobStatus').value, 10) },
    };

    fetch('/api/jobs', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })
      .then((r) => {
        if (!r.ok) throw new Error('Create failed');
        return r.json();
      })
      .then((created) => {
        createModal.hide();
        // Try to refresh calendar if present, else reload
        if (window.refreshCalendarWithJob && typeof window.refreshCalendarWithJob === 'function') {
          window.refreshCalendarWithJob(created);
        } else {
          window.location.reload();
        }
      })
      .catch((err) => {
        console.error(err);
        alert('Failed to create job');
      });
  });

  // Optional: expose an opener for pages with a button
  window.openCreateJobModal = function () {
    document.getElementById('createJobForm')?.reset();
    createModal.show();
  };
})();
