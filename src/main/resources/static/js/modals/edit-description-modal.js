// edit-description-modal.js
(function () {
    const modalEl = document.getElementById('descModal');
    if (!modalEl) return;

    const descModal = new bootstrap.Modal(modalEl);

    document.getElementById('editDescForm')?.addEventListener('submit', (e) => {
        e.preventDefault();
        const id = document.getElementById('jobIdDesc').value;
        const payload = {
            job_description: document.getElementById('job_description_desc').value
        };

        fetch('/api/jobs/' + id + '/description', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        })
            .then(r => {
                if (!r.ok) throw new Error('Update description failed');
                return r.json?.() ?? {};
            })
            .then(() => {
                descModal.hide();
                const p = document.getElementById('jobDescriptionText');
                if (p) p.textContent = payload.job_description;
            })
            .catch(err => {
                console.error(err);
                alert('Kunne ikke opdatere beskrivelsen.');
            });
    });

    window.openDescriptionModal = function (jobId, currentText) {
        const ta = document.getElementById('job_description_desc');
        const idEl = document.getElementById('jobIdDesc');
        if (idEl) idEl.value = jobId;
        if (ta) ta.value = currentText || '';
        descModal.show();
    };
})();