document.addEventListener('DOMContentLoaded', function () {
    // Consts for active or completed filter buttons
    const completedBtn = document.getElementById('completed-btn');
    const activeBtn = document.getElementById('active-btn');
    const tableBody = document.getElementById('table-body');

    // Make each <tr data-href> row clickable and navigate to the URL in data-href
    const rows = document.querySelectorAll('tr[data-href]');
    rows.forEach((row) => {
        row.addEventListener('click', () => {
            window.location.href = row.getAttribute('data-href');
        });
    });

    // Convert plain status text into a visually distinct badge using helper functions if available
    formatStatusBadges();

    // Use the modal module's openCreateJobModal if provided
    document.getElementById('openCreateJobBtn')?.addEventListener('click', () => {
        if (window.openCreateJobModal) window.openCreateJobModal();
    });

    // Filter for sorting active and completed jobs
    // Normalize the job statuses to string and clean
    function normalizeStatus(s) {
        return (s || '').toString().trim().toLowerCase().replace(/[_-]/g, ' ');
    }

    // Function for checking whether the status is "afhentet" or not
    // and returns string if it is "afhentet"
    function isPickedUp(status) {
        const st = normalizeStatus(status);
        return /picked\s*up/.test(st) || st === 'afhentet' || st === 'pickedup';
    }

    function getRowStatus(row) {
        if (row.dataset && row.dataset.status) return normalizeStatus(row.dataset.status);
        const el = row.querySelector('.job-status');
        return el ? normalizeStatus(el.textContent) : ';';
    }

    // Function for checking whether the status is "afhentet"
    // and then hiding or showing job depending on status
    function applyFilter(filter) {
        if (!tableBody) return;
        const rows = tableBody.querySelectorAll('tr');
        rows.forEach((row) => {
            const status = getRowStatus(row);
            const show = filter === 'active' ? !isPickedUp(status) : filter === 'completed' ? isPickedUp(status) : true;
            row.classList.toggle('d-none', !show);
        });
        activeBtn?.classList.toggle('active', filter === 'active');
        completedBtn?.classList.toggle('active', filter === 'completed');
    }
    applyFilter('active');

    // Listens for filter buttons
    activeBtn?.addEventListener('click', () => applyFilter('active'));
    completedBtn?.addEventListener('click', () => applyFilter('completed'));
});

// Eventlistener for the seach-input form
document.getElementById('search-input').addEventListener('submit', async (e) => {
    e.preventDefault();
    let matches;

    // Get search params from the input field
    let searchParam = document.getElementById('searchparams').value;

    // If the search bar input is empty, show all repairs
    if (!searchParam) {
        matches = await fetchAllRepairs();
    } else {
        matches = await fetchSearchMatches(searchParam);
    }

    // Remove existing elements
    let tablebody = document.getElementById('table-body');
    tablebody.innerHTML = '';

    // If no matches found
    if (!matches[0]) {
        let noMatchesMessage = document.createElement('td');
        noMatchesMessage.innerHTML = 'Ingen reparationer fundet...';
        noMatchesMessage.className = 'w-20';
        tablebody.appendChild(noMatchesMessage);
    } else {
        // Create a new row for each search match
        matches.forEach((match) => {
            let newRow = document.createElement('tr');
            newRow.style.cursor = 'pointer';
            newRow.dataset.href = `/jobliste/${match.id}`;
            newRow.addEventListener('click', () => (window.location.href = newRow.dataset.href));

            newRow.innerHTML = `
                  <td class="w-15">
                    <div class="d-flex flex-column gap-1">
                      <p class="fw-bolder mb-1">${match.title || ''}</p>
                      <p class="mb-0">${match.customer_name || ''}</p>
                      <p>${match.customer_phone || ''}</p>
                    </div>
                  </td>
                  <td class="w-15">
                    <div>
                      <span class="job-status" >${match.status.name || ''}</span>
                    </div>
                  </td>
                  <td class="w-30">
                    <div class="w-75">
                      <p>${match.job_description || ''}</p>
                    </div>
                  </td>
                  <td class="w-30">${formatDate(match.date || '')}</td>
                `;

            // Append to the table-body
            tablebody.appendChild(newRow);
        });

        // Reformat badges
        formatStatusBadges();

        // Highlight the inputfields text, so that the user quickly can perform a new search
        document.getElementById('searchparams').select();
    }
});

// Function to format the date
function formatDate(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleString('da-DK', {
        day: 'numeric',
        month: 'long',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
    });
}

async function fetchAllRepairs() {
    try {
        // Send PUT request to update the job entry
        const r1 = await fetch('/api/jobs', {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' },
        });

        // Get the matches from the response and update the page to show matches
        return await r1.json();
    } catch (err) {
        // todo: add some error handling
        console.log(err);
    }
}

async function fetchSearchMatches(searchParam) {
    try {
        // Send PUT request to update the job entry
        const r1 = await fetch('/api/search/job?q=' + searchParam, {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' },
        });

        // Get the matches from the response and update the page to show matches
        return await r1.json();
    } catch (err) {
        // todo: add some error handleing
        console.log(err);
    }
}

function formatStatusBadges() {
    document.querySelectorAll('.job-status').forEach((el) => {
        const raw = el.textContent.trim();
        // statusToColors returns an object { borderColor, backgroundColor, textColor, cssClass }
        const colors = typeof statusToColors === 'function' ? statusToColors(raw) : {};
        // translateStatusName is optional and maps internal status names to human-readable labels
        el.textContent = typeof translateStatusName === 'function' ? translateStatusName(raw) : raw;
        Object.assign(el.style, {
            display: 'inline-block',
            padding: '2px 8px',
            borderRadius: '3px',
            border: `1px solid ${colors.borderColor || '#ccc'}`,
            backgroundColor: colors.backgroundColor || '#eee',
            color: colors.textColor || '#000',
            fontWeight: '600',
        });
        if (colors.cssClass) el.classList.add(colors.cssClass);
    });
}
