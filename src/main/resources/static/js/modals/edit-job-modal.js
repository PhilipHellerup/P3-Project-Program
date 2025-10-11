(function () {
  const modalEl = document.getElementById('editJobModal');
  if (!modalEl) return;

  const editModal = new bootstrap.Modal(modalEl);

  function toIsoLocal(val) {
    if (!val) return null;
    return val.length === 16 ? val + ':00' : val;
  }

  document.getElementById('editJobForm')?.addEventListener('submit', (e) => {
    e.preventDefault();
    const id = document.getElementById('jobId').value;
    const payload = {
      title: document.getElementById('title').value,
      customer_name: document.getElementById('customer_name').value,
      customer_phone: document.getElementById('customer_phone').value,
      work_time_minutes: parseInt(document.getElementById('work_time_minutes').value || '0', 10),
      price_per_minute: parseFloat(document.getElementById('price_per_minute').value || '0'),
      date: toIsoLocal(document.getElementById('date').value),
      status: { id: parseInt(document.getElementById('status_id').value, 10) },
    };

    fetch('/api/jobs/' + id, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })
      .then((r) => {
        if (!r.ok) throw new Error('Update failed');
        return r.json();
      })
      .then(() => {
        editModal.hide();
        window.location.reload();
      })
      .catch((err) => {
        console.error(err);
        alert('Kunne ikke opdatere jobbet.');
      });
  });

  // Fill modal fields and open
  window.openEditJobModal = function (job) {
    document.getElementById('jobId').value = job.id;
    document.getElementById('title').value = job.title || '';
    document.getElementById('customer_name').value = job.customer_name || '';
    document.getElementById('customer_phone').value = job.customer_phone || '';
    document.getElementById('work_time_minutes').value = job.work_time_minutes ?? '';
    document.getElementById('price_per_minute').value = job.price_per_minute ?? '';
    document.getElementById('date').value = job.date
      ? job.date.length === 16
        ? job.date
        : job.date.slice(0, 16)
      : '';
    document.getElementById('status_id').value = job.status?.id || job.status_id || 1;
    editModal.show();
  };
})();
