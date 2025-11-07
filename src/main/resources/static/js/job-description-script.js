document.addEventListener('DOMContentLoaded', () => {
    const el = document.getElementById('job-status');
    if (el) {
        const raw = el.textContent?.trim() ?? '';

        // Format the label if a translation function is available on window
        const label = typeof window.translateStatusName === 'function' ? window.translateStatusName(raw) : raw;

        // Get colors mapping (border/background/text) if provided by the app
        const colors = typeof window.statusToColors === 'function' ? window.statusToColors(raw) : {};

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
        // Collect job values using Thymeleaf inlined expressions (note: these become raw values on the client)
        const job = {
            id: '[[${job.id}]]' || '',
            title: '[[${job.title}]]',
            customer_name: '[[${job.customer_name}]]',
            customer_phone: '[[${job.customer_phone}]]',
            work_time_minutes: '[[${job.work_time_minutes}]]' || 0,
            price_per_minute: '[[${job.price_per_minute}]]' || 0,
            date: "[[${#temporals.format(job.date, 'yyyy-MM-dd''T''HH:mm')}]]" || '',
            status: { id: '[[${job.status.id}]]' || 1 },
        };
        // Call modal opener if available
        if (window.openEditJobModal) window.openEditJobModal(job);
    });

    // Open Description modal and pass current description text
    document.getElementById('openDescBtn')?.addEventListener('click', (e) => {
        e.preventDefault();
        const id = '[[${job.id}]]' || '';
        const current = document.getElementById('jobDescriptionText')?.textContent ?? '';
        if (window.openDescriptionModal) window.openDescriptionModal(id, current.trim());
    });

    // Event listener for the "tilføj product" btn
    // todo: show to the user that the product was added
    // todo: do some data validation and error handling
    document.getElementById('add-product-btn').addEventListener('click', async (e) => {
        window.openAddProductToRepairModal()
    })

});
