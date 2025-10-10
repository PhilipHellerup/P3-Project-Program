(function () {
    const modalEl = document.getElementById('fullEditJobModal');
    if (!modalEl) return;

    const fullModal = new bootstrap.Modal(modalEl);

    function toIsoLocal(val) {
        if (!val) return null;
        return val.length === 16 ? val + ':00' : val;
    }

    document.getElementById('fullEditJobForm')?.addEventListener('submit', async (e) => {
        e.preventDefault();

        const id = document.getElementById('full_jobId').value;
        const Payload = {
            title: document.getElementById('full_title').value,
            customer_name: document.getElementById('full_customer_name').value,
            customer_phone: document.getElementById('full_customer_phone').value,
            job_description: document.getElementById('full_job_description').value || '',
            work_time_minutes: parseInt(document.getElementById('full_work_time_minutes').value || '0', 10),
            price_per_minute: parseFloat(document.getElementById('full_price_per_minute').value || '0'),
            date: toIsoLocal(document.getElementById('full_date').value),
            status: { id: parseInt(document.getElementById('full_status_id').value, 10) }
        };
        try {
            const r1 = await fetch('/api/jobs/' + id, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(Payload)
            });
            if (!r1.ok) throw new Error('Kunne ikke opdatere jobbet');

            fullModal.hide();
            window.location.reload();
        } catch (err) {
            console.error(err);
            alert(err.message || 'Opdatering fejlede.');
        }
    });

    // Open and prefill
    window.openFullEditJobModal = function (job) {
        document.getElementById('full_jobId').value = job.id;
        document.getElementById('full_title').value = job.title || '';
        document.getElementById('full_customer_name').value = job.customer_name || '';
        document.getElementById('full_customer_phone').value = job.customer_phone || '';
        document.getElementById('full_work_time_minutes').value = job.work_time_minutes ?? '';
        document.getElementById('full_price_per_minute').value = job.price_per_minute ?? '';
        document.getElementById('full_date').value = job.date ? (job.date.length === 16 ? job.date : job.date.slice(0, 16)) : '';
        document.getElementById('full_status_id').value = (job.status?.id || job.status_id || 1);
        document.getElementById('full_job_description').value = job.job_description || '';
        fullModal.show();
    };
})();