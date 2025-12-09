// Call this function when the DOM content is all loaded
document.addEventListener('DOMContentLoaded', function () {
    // Ensure document.body exists (some browsers fire DOMContentLoaded early with null body in edge cases)
    if (!document.body) {
        document.addEventListener('readystatechange', function onReady() {
            if (document.readyState === 'complete' && document.body) {
                document.removeEventListener('readystatechange', onReady);
                initNavbar();
            }
        });
        return;
    }

    // Initiate the navbar. This is called on the calendar, list, and product page to show the navbar
    initNavbar();

    // Definition of the navbar initiation function
    function initNavbar() {
        // Ensure the CSS link to the navbar css stylesheet exists and load it
        function ensureNavbarCss() {
            return new Promise((resolve, reject) => {
                // Use root-relative path to avoid page-relative resolutions
                const href = '/css/navbar.css';

                // If already present, resolve when it's loaded
                const existing = Array.from(
                    document.head.querySelectorAll('link[rel="stylesheet"]'),
                ).find((l) => (l.getAttribute('href') || l.href) === href);
                if (existing) {
                    // If it's already loaded, resolve immediately
                    if (existing.sheet) {
                        return resolve();
                    }
                    // otherwise wait for load/error
                    existing.addEventListener('load', () => resolve(), { once: true });
                    existing.addEventListener('error', (e) => reject(e), { once: true });
                    // and give a fallback timeout
                    setTimeout(() => resolve(), 1500);
                    return;
                }

                const link = document.createElement('link');
                link.rel = 'stylesheet';
                link.href = href;
                link.addEventListener('load', () => resolve(), { once: true });
                link.addEventListener('error', (e) => reject(e), { once: true });
                document.head.appendChild(link);

                // safety fallback: if load doesn't fire in reasonable time, resolve so UI still appears
                setTimeout(() => {
                    if (!link.sheet) {
                        console.warn('navbar.css did not load within timeout — continuing anyway');
                    }
                    resolve();
                }, 2500);
            });
        }

        // The function that inserts the navbar on the page
        function insertNavbar() {
            // Ensure that the navbar is not already present on the page
            if (document.querySelector('.sidebar-nav')) return;

            const navbarHTML = `
            <nav class="sidebar-nav" aria-hidden="false">
                <div class="nav-header">
                  <a href="/kalender">
                    <img src="/images/Perform_logo_white.avif" alt="Perform" class="logo">
                  </a>
                </div>
                <ul class="nav-menu">
                    <!-- <li class="nav-item"><a class="nav-link" href="/"><span class="nav-text">Hjem</span></a></li> -->
                    <li class="nav-item"><a class="nav-link" href="/kalender"><span class="nav-text">Kalender</span></a></li>
                    <li class="nav-item"><a class="nav-link" href="/jobliste"><span class="nav-text">Reparationsliste</span></a></li>
                    <li class="nav-item"><a class="nav-link" href="/products"><span class="nav-text">Produkter</span></a></li>
                </ul>
            </nav>
        `;

            // Insert the navbar on the page
            document.body.insertAdjacentHTML('afterbegin', navbarHTML);
            const navbar = document.querySelector('.sidebar-nav');
            document.body.classList.add('with-navbar', 'content-push');
            navbar.style.transform = 'translateX(0)';
        }

        // Ensure CSS is loaded before inserting the navbar (prevents flash / 404 issues due to relative path)
        ensureNavbarCss()
            .then(() => insertNavbar())
            .catch((err) => {
                console.error('Failed to load navbar CSS:', err);
                // Still insert navbar so functionality isn't blocked — user will see unstyled until stylesheet loads
                insertNavbar();
            });
    }
});
